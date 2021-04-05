function loadFullReplay(){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/fullReplay'+location.search)
  xhr.onload = () => {
    if(xhr.response){
      var fullReplay = JSON.parse(xhr.response)
      cells = fullReplay.cells
      replay = fullReplay.replay
      movesCount = replay.moves.length
      gameSettings = replay.settings
      if(replay.winner === "DRAW"){
        winnerText.innerHTML = "Итог игры: ничья"
      }else if(replay.winner === "WHITE"){
        winnerText.innerHTML = "Итог игры: победа белых"
      }else if(replay.winner === "BLACK"){
        winnerText.innerHTML = "Итог игры: победа черных"
      }

      addFiguresToBoard(fullReplay.figures)
      updateBoard(false)
    }
  }
  xhr.send()
}

function updateBoard(redraw){
  prevMoveBtn.disabled = moveNum === 0
  nextMoveBtn.disabled = moveNum === movesCount
  moveNumText.innerHTML = "Ход " + (moveNum+1) + " из " + (movesCount+1)
  var activePlayer = gameSettings.firstPlayer === "WHITE" ? 0 : 1
  activePlayer = (activePlayer+moveNum)%2
  if(activePlayer == 0){
    whitePlayerUsername.style.color = "blue"
    blackPlayerUsername.style.color = "black"
  }else{
    whitePlayerUsername.style.color = "black"
    blackPlayerUsername.style.color = "blue"
  }

  if(redraw) drawBoard()
}

function prevMove(){
  if(moveNum > 0){
    moveNum--
    var move = replay.moves[moveNum]
    cells[move.from].figure = cells[move.to].figure
    cells[move.from].figure.cell = move.from
    cells[move.to].figure = move.toFigure
    updateBoard(true)
  }
}

function nextMove(){
  if(moveNum < movesCount){
    var move = replay.moves[moveNum]
    replay.moves[moveNum].toFigure = cells[move.to].figure
    cells[move.from].figure.cell = move.to
    cells[move.to].figure = cells[move.from].figure
    cells[move.from].figure = null
    moveNum++
    updateBoard(true)
  }
}

var replay = null
var moveNum = 0
var moveCount = 0
var prevMoveBtn = document.getElementById('prev-move-btn')
prevMoveBtn.addEventListener("click", prevMove)
var nextMoveBtn = document.getElementById('next-move-btn')
nextMoveBtn.addEventListener("click", nextMove)
var moveNumText = document.getElementById('move-num-text')
var winnerText = document.getElementById('winner-text')
var whitePlayerUsername = document.getElementById("white-player-username")
var blackPlayerUsername = document.getElementById("black-player-username")

loadUserSettings(loadFullReplay)