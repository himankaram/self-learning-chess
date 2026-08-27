/* =========================================================================
   CHESS COACH — full client-side port of the Java game:
   board-aware move generation, self-check filtering (an improvement over
   the original desktop version), simulation-based checkmate/stalemate,
   a safety-aware AI opponent, and a coach that comments on your moves and
   can suggest one on request.
   Board convention: row 0 = rank 8 (black's back rank), row 7 = rank 1
   (white's back rank). Columns 0-7 = files a-h. Standard orientation.
   ========================================================================= */

const PIECE_GLYPHS = {
  white: { P: "♙", N: "♘", B: "♗", R: "♖", Q: "♕", K: "♔" },
  black: { P: "♟", N: "♞", B: "♝", R: "♜", Q: "♛", K: "♚" }
};

const PIECE_VALUES = { P: 1, N: 3, B: 3, R: 5, Q: 9, K: 1000 };
const PIECE_NAMES = { P: "Pawn", N: "Knight", B: "Bishop", R: "Rook", Q: "Queen", K: "King" };

function inBounds(r, c) { return r >= 0 && r <= 7 && c >= 0 && c <= 7; }
function enemyOf(color) { return color === "white" ? "black" : "white"; }
function fileRank(row, col) { return "abcdefgh"[col] + (8 - row); }

/* ---------------------------------------------------------------------
   BOARD SETUP
   --------------------------------------------------------------------- */

function createInitialBoard() {
  const board = Array.from({ length: 8 }, () => Array(8).fill(null));
  const backRank = ["R", "N", "B", "Q", "K", "B", "N", "R"];

  for (let c = 0; c < 8; c++) {
    board[0][c] = { type: backRank[c], color: "black" };
    board[1][c] = { type: "P", color: "black" };
    board[6][c] = { type: "P", color: "white" };
    board[7][c] = { type: backRank[c], color: "white" };
  }
  return board;
}

function cloneBoard(board) {
  return board.map(row => row.slice());
}

/* ---------------------------------------------------------------------
   MOVE GENERATION (board-aware: blocks, captures, no landing on own piece)
   --------------------------------------------------------------------- */

function pseudoLegalMoves(board, row, col) {
  const piece = board[row][col];
  if (!piece) return [];
  const moves = [];
  const color = piece.color;

  function tryAdd(r, c) {
    if (!inBounds(r, c)) return;
    if (board[r][c] === null || board[r][c].color !== color) moves.push({ row: r, col: c });
  }

  function slide(directions) {
    for (const [dr, dc] of directions) {
      let r = row + dr, c = col + dc;
      while (inBounds(r, c)) {
        if (board[r][c] === null) {
          moves.push({ row: r, col: c });
        } else {
          if (board[r][c].color !== color) moves.push({ row: r, col: c });
          break;
        }
        r += dr; c += dc;
      }
    }
  }

  switch (piece.type) {
    case "R":
      slide([[0,1],[0,-1],[1,0],[-1,0]]);
      break;
    case "B":
      slide([[1,1],[1,-1],[-1,1],[-1,-1]]);
      break;
    case "Q":
      slide([[0,1],[0,-1],[1,0],[-1,0],[1,1],[1,-1],[-1,1],[-1,-1]]);
      break;
    case "N":
      for (const [dr, dc] of [[1,2],[-1,2],[1,-2],[-1,-2],[2,1],[-2,1],[2,-1],[-2,-1]]) {
        tryAdd(row + dr, col + dc);
      }
      break;
    case "K":
      for (const [dr, dc] of [[0,1],[0,-1],[1,0],[-1,0],[1,1],[1,-1],[-1,1],[-1,-1]]) {
        tryAdd(row + dr, col + dc);
      }
      break;
    case "P": {
      const dir = color === "white" ? -1 : 1; // white moves up (toward row 0)
      const startRow = color === "white" ? 6 : 1;

      // forward
      if (inBounds(row + dir, col) && board[row + dir][col] === null) {
        moves.push({ row: row + dir, col });
        if (row === startRow && board[row + 2 * dir][col] === null) {
          moves.push({ row: row + 2 * dir, col });
        }
      }
      // diagonal captures only
      for (const dc of [-1, 1]) {
        const r = row + dir, c = col + dc;
        if (inBounds(r, c) && board[r][c] !== null && board[r][c].color !== color) {
          moves.push({ row: r, col: c });
        }
      }
      break;
    }
  }
  return moves;
}

function isSquareAttacked(board, row, col, byColor) {
  for (let r = 0; r <= 7; r++) {
    for (let c = 0; c <= 7; c++) {
      const p = board[r][c];
      if (p && p.color === byColor) {
        for (const m of pseudoLegalMoves(board, r, c)) {
          if (m.row === row && m.col === col) return true;
        }
      }
    }
  }
  return false;
}

function findKing(board, color) {
  for (let r = 0; r <= 7; r++)
    for (let c = 0; c <= 7; c++)
      if (board[r][c] && board[r][c].type === "K" && board[r][c].color === color)
        return { row: r, col: c };
  return null;
}

function isKingInCheck(board, color) {
  const king = findKing(board, color);
  if (!king) return false;
  return isSquareAttacked(board, king.row, king.col, enemyOf(color));
}

function simulateMove(board, from, to) {
  const next = cloneBoard(board);
  let moving = next[from.row][from.col];
  // auto-queen promotion, a small correctness improvement over the desktop version
  if (moving.type === "P" && (to.row === 0 || to.row === 7)) {
    moving = { type: "Q", color: moving.color };
  }
  next[to.row][to.col] = moving;
  next[from.row][from.col] = null;
  return next;
}

function legalMoves(board, row, col) {
  const piece = board[row][col];
  if (!piece) return [];
  return pseudoLegalMoves(board, row, col).filter(m => {
    const after = simulateMove(board, { row, col }, m);
    return !isKingInCheck(after, piece.color);
  });
}

function allLegalMoves(board, color) {
  const result = [];
  for (let r = 0; r <= 7; r++) {
    for (let c = 0; c <= 7; c++) {
      const p = board[r][c];
      if (p && p.color === color) {
        for (const m of legalMoves(board, r, c)) {
          result.push({ from: { row: r, col: c }, to: m });
        }
      }
    }
  }
  return result;
}

function isCheckmate(board, color) {
  return isKingInCheck(board, color) && allLegalMoves(board, color).length === 0;
}
function isStalemate(board, color) {
  return !isKingInCheck(board, color) && allLegalMoves(board, color).length === 0;
}

/* ---------------------------------------------------------------------
   SHARED MOVE EVALUATOR — used by both the AI and the human hint feature
   --------------------------------------------------------------------- */

function evaluateMoveScore(board, from, to, color) {
  const moving = board[from.row][from.col];
  const target = board[to.row][to.col];
  const enemy = enemyOf(color);

  let score = 0;
  if (target) score += PIECE_VALUES[target.type] * 10;

  const wasInDanger = isSquareAttacked(board, from.row, from.col, enemy);
  const after = simulateMove(board, from, to);
  const landsInDanger = isSquareAttacked(after, to.row, to.col, enemy);

  if (landsInDanger) score -= PIECE_VALUES[moving.type] * 10;
  if (wasInDanger && !landsInDanger) score += PIECE_VALUES[moving.type] * 8;

  return score;
}

function pickAIMove(board, color, difficulty) {
  const moves = allLegalMoves(board, color);
  if (moves.length === 0) return null;

  if (difficulty === "easy" && Math.random() < 0.4) {
    return moves[Math.floor(Math.random() * moves.length)];
  }

  let best = moves[0];
  let bestScore = -Infinity;
  for (const move of moves) {
    let score = evaluateMoveScore(board, move.from, move.to, color);
    if (difficulty === "hard") {
      const central = (move.to.row === 3 || move.to.row === 4) && (move.to.col === 3 || move.to.col === 4);
      if (central) score += 4;
    }
    if (score > bestScore) { bestScore = score; best = move; }
  }
  return best;
}

/* ---------------------------------------------------------------------
   GAME STATE
   --------------------------------------------------------------------- */

const state = {
  board: createInitialBoard(),
  turn: "white",
  humanColor: "white",
  difficulty: "medium",
  selected: null,
  legalForSelected: [],
  gameOver: false,
  capturedByWhite: [], // black pieces white has taken
  capturedByBlack: []  // white pieces black has taken
};

/* ---------------------------------------------------------------------
   COACH MESSAGES
   --------------------------------------------------------------------- */

function addNote(text, cls) {
  const log = document.getElementById("chatLog");
  const div = document.createElement("div");
  div.className = "note" + (cls ? " " + cls : "");
  div.textContent = text;
  log.appendChild(div);
  log.scrollTop = log.scrollHeight;
}

function giveMoveAdvice(movingType, capturedPiece, from, to) {
  const name = PIECE_NAMES[movingType];
  let msg;

  if (capturedPiece) {
    msg = `Your ${name} captured the ${PIECE_NAMES[capturedPiece.type]}. `;
    const gained = PIECE_VALUES[capturedPiece.type] - PIECE_VALUES[movingType];
    msg += gained >= 0
      ? "That looks like a good trade — you gave up less material than you won."
      : "Worth double-checking: you used a more valuable piece to take a less valuable one. Was there a reason (removing a defender, stopping a threat)?";
  } else {
    msg = `You moved your ${name} from ${fileRank(from.row, from.col)} to ${fileRank(to.row, to.col)}. `;
    const central = (to.row === 3 || to.row === 4) && (to.col === 3 || to.col === 4);
    if (movingType === "P" && central) {
      msg += "Good instinct — controlling the center with a pawn early on gives your other pieces more options.";
    } else if (movingType === "N" || movingType === "B") {
      const backRank = from.row === 0 || from.row === 7;
      msg += backRank
        ? "Nice, that's called 'developing' a piece — getting it off the back rank early is usually strong."
        : "Think about which squares this piece now covers, and whether it's protected if it's attacked.";
    } else if (movingType === "K") {
      msg += "Moving the king early can be risky since it's usually safer tucked behind your other pieces — make sure this was intentional.";
    } else {
      msg += "Consider what this move opens up or defends, and whether your opponent has a strong reply.";
    }
  }
  addNote(msg);
}

function opponentFeedback(movingType, capturedPiece, to) {
  let msg = "Opponent: ";
  if (capturedPiece) {
    msg += `Took your ${PIECE_NAMES[capturedPiece.type]} with a ${PIECE_NAMES[movingType]}. Was that piece defended? Worth checking before your next move.`;
  } else {
    msg += `Moved a ${PIECE_NAMES[movingType]} to ${fileRank(to.row, to.col)}. Take a look at what squares it now covers before you respond.`;
  }
  addNote(msg, "opponent");
}

function giveHint() {
  if (state.gameOver || state.turn !== state.humanColor) {
    addNote("Hang on for your turn first!", "system");
    return;
  }
  const moves = allLegalMoves(state.board, state.humanColor);
  if (moves.length === 0) {
    addNote("I don't see a legal move available for you right now.", "system");
    return;
  }

  let best = moves[0];
  let bestScore = -Infinity;
  for (const move of moves) {
    let score = evaluateMoveScore(state.board, move.from, move.to, state.humanColor);
    const central = (move.to.row === 3 || move.to.row === 4) && (move.to.col === 3 || move.to.col === 4);
    if (central) score += 4;
    if (score > bestScore) { bestScore = score; best = move; }
  }

  const moving = state.board[best.from.row][best.from.col];
  const target = state.board[best.to.row][best.to.col];
  const name = PIECE_NAMES[moving.type];

  let msg = `If I were you, I'd move the ${name} from ${fileRank(best.from.row, best.from.col)} to ${fileRank(best.to.row, best.to.col)}. `;
  if (target) {
    msg += `It captures the ${PIECE_NAMES[target.type]} and, as far as I can see, doesn't lose material back.`;
  } else {
    const enemy = enemyOf(state.humanColor);
    const wasInDanger = isSquareAttacked(state.board, best.from.row, best.from.col, enemy);
    const central = (best.to.row === 3 || best.to.row === 4) && (best.to.col === 3 || best.to.col === 4);
    if (wasInDanger) msg += "That piece was under attack where it stood — this gets it to safety.";
    else if (central) msg += "It helps take control of the center, which is usually a strong plan early on.";
    else msg += "It looks like a safe, useful move from what I can calculate — but I'm only weighing material and safety, not deep strategy.";
  }
  addNote(msg, "hint");
}

/* ---------------------------------------------------------------------
   RENDERING
   --------------------------------------------------------------------- */

function render() {
  const boardEl = document.getElementById("board");
  boardEl.innerHTML = "";

  const inCheckColor = ["white", "black"].find(c => isKingInCheck(state.board, c));
  const kingSquare = inCheckColor ? findKing(state.board, inCheckColor) : null;

  for (let r = 0; r <= 7; r++) {
    for (let c = 0; c <= 7; c++) {
      const sq = document.createElement("div");
      const light = (r + c) % 2 === 0;
      sq.className = "square " + (light ? "light" : "dark");

      const piece = state.board[r][c];
      if (piece) {
        sq.classList.add(piece.color + "-piece");
        const span = document.createElement("span");
        span.className = "piece";
        span.textContent = PIECE_GLYPHS[piece.color][piece.type];
        sq.appendChild(span);
      }

      if (state.selected && state.selected.row === r && state.selected.col === c) {
        sq.classList.add("selected");
      }
      if (state.legalForSelected.some(m => m.row === r && m.col === c)) {
        sq.classList.add(state.board[r][c] ? "legal-capture" : "legal-move");
      }
      if (kingSquare && kingSquare.row === r && kingSquare.col === c) {
        sq.classList.add("king-in-check");
      }

      sq.addEventListener("click", () => onSquareClick(r, c));
      boardEl.appendChild(sq);
    }
  }

  renderCaptured();
  renderStatus(inCheckColor);
}

function renderCaptured() {
  const w = document.getElementById("capturedByWhite");
  const b = document.getElementById("capturedByBlack");
  w.textContent = state.capturedByWhite.map(p => PIECE_GLYPHS.black[p.type]).join(" ");
  b.textContent = state.capturedByBlack.map(p => PIECE_GLYPHS.white[p.type]).join(" ");
}

function renderStatus(inCheckColor) {
  const statusEl = document.getElementById("boardStatus");
  if (state.gameOver) return;
  const whoseTurn = state.turn === state.humanColor ? "Your move" : "Opponent thinking";
  statusEl.textContent = `${whoseTurn} — ${state.turn === "white" ? "White" : "Black"}`;
  statusEl.classList.toggle("check", !!inCheckColor);
  if (inCheckColor) statusEl.textContent += ` — ${inCheckColor === "white" ? "White" : "Black"} is in check`;
}

/* ---------------------------------------------------------------------
   INTERACTION
   --------------------------------------------------------------------- */

function onSquareClick(row, col) {
  if (state.gameOver || state.turn !== state.humanColor) return;

  const clickedPiece = state.board[row][col];

  // Nothing selected yet
  if (!state.selected) {
    if (clickedPiece && clickedPiece.color === state.turn) {
      state.selected = { row, col };
      state.legalForSelected = legalMoves(state.board, row, col);
      render();
    }
    return;
  }

  // Clicking the same square again deselects
  if (state.selected.row === row && state.selected.col === col) {
    state.selected = null;
    state.legalForSelected = [];
    render();
    return;
  }

  // Clicking another of your own pieces reselects
  if (clickedPiece && clickedPiece.color === state.turn) {
    state.selected = { row, col };
    state.legalForSelected = legalMoves(state.board, row, col);
    render();
    return;
  }

  // Is this square a legal destination for the selected piece?
  const isLegal = state.legalForSelected.some(m => m.row === row && m.col === col);
  if (!isLegal) {
    state.selected = null;
    state.legalForSelected = [];
    render();
    return;
  }

  playHumanMove(state.selected, { row, col });
}

function playHumanMove(from, to) {
  const moving = state.board[from.row][from.col];
  const captured = state.board[to.row][to.col];

  state.board = simulateMove(state.board, from, to);
  if (captured) state.capturedByWhite.push(captured);

  state.selected = null;
  state.legalForSelected = [];

  giveMoveAdvice(moving.type, captured, from, to);
  render();

  const opponent = enemyOf(state.humanColor);
  if (isCheckmate(state.board, opponent)) {
    endGame(`Checkmate — ${state.humanColor === "white" ? "White" : "Black"} wins`);
    return;
  }
  if (isStalemate(state.board, opponent) || allLegalMoves(state.board, opponent).length === 0) {
    endGame("Game is a draw");
    return;
  }

  state.turn = opponent;
  render();
  setTimeout(playAIMove, 700);
}

function playAIMove() {
  if (state.gameOver) return;
  const aiColor = enemyOf(state.humanColor);
  const move = pickAIMove(state.board, aiColor, state.difficulty);
  if (!move) return;

  const moving = state.board[move.from.row][move.from.col];
  const captured = state.board[move.to.row][move.to.col];

  state.board = simulateMove(state.board, move.from, move.to);
  if (captured) state.capturedByBlack.push(captured);

  opponentFeedback(moving.type, captured, move.to);

  const human = state.humanColor;
  if (isCheckmate(state.board, human)) {
    render();
    endGame(`Checkmate — ${aiColor === "white" ? "White" : "Black"} wins`);
    return;
  }
  if (isStalemate(state.board, human) || allLegalMoves(state.board, human).length === 0) {
    render();
    endGame("Game is a draw");
    return;
  }

  state.turn = human;
  render();
}

function endGame(message) {
  state.gameOver = true;
  render();
  const backdrop = document.getElementById("modalBackdrop");
  document.getElementById("modalText").textContent = message;
  backdrop.classList.add("visible");
  addNote(message, "system");
}

function newGame() {
  state.board = createInitialBoard();
  state.turn = "white";
  state.selected = null;
  state.legalForSelected = [];
  state.gameOver = false;
  state.capturedByWhite = [];
  state.capturedByBlack = [];
  document.getElementById("chatLog").innerHTML = "";
  document.getElementById("modalBackdrop").classList.remove("visible");
  addNote("Welcome! Make a move and I'll explain what it does and what to look for next.", "system");
  render();
}

/* ---------------------------------------------------------------------
   WIRING
   --------------------------------------------------------------------- */

document.getElementById("difficulty").addEventListener("change", e => {
  state.difficulty = e.target.value;
});
document.getElementById("hintBtn").addEventListener("click", giveHint);
document.getElementById("newGameBtn").addEventListener("click", newGame);
document.getElementById("modalCloseBtn").addEventListener("click", newGame);

newGame();
