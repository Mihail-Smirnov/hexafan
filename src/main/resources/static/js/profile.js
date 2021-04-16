function showMessage(header, message){
  messageHeader.innerHTML = header
  messageText.innerHTML = message
  messageDialog.showModal()
}

function closeMessageDialog(){
  messageDialog.close()
}

function getCheckedValue(elementName){
  var rad = document.getElementsByName(elementName)
  for (var i = 0; i < rad.length; i++) {
    if (rad[i].checked) {
	    return rad[i].value
    }
  }
}

function getTimeString(seconds){
  return Math.floor(seconds/3600).toString().padStart(2, '0') + ":"
  + Math.floor(seconds/60%60).toString().padStart(2, '0') + ":"
  + Math.floor(seconds%60).toString().padStart(2, '0')
}

function getNumericEnding(num){
  num = Math.floor(num)
  if(num % 10 >= 5 || (num >= 10 && num <= 19)){
    return 2
  }else if(num % 10 == 1){
    return 0
  }else{
    return 1
  }
}

var secondsString = ["секунду", "секунды", "секунд"]
var minutesString = ["минуту", "минуты", "минут"]
var hoursString = ["час", "часа", "часов"]
var daysString = ["день", "дня", "дней"]

function getTimeAgoString(milliseconds){
  var seconds = milliseconds / 1000
  var minutes = seconds / 60
  var hours = minutes / 60
  var days = hours / 24
  if(seconds < 60){
    return Math.floor(seconds) + " " + secondsString[getNumericEnding(seconds)] + " назад"
  }else if(minutes < 60){
    return Math.floor(minutes) + " " + minutesString[getNumericEnding(minutes)] + " назад"
  }else if(hours < 24){
    return Math.floor(hours) + " " + hoursString[getNumericEnding(hours)] + " назад"
  }else{
    return Math.floor(days) + " " + daysString[getNumericEnding(days)] + " назад"
  }
}

function loadUserStats(callback){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/currentUserStats')
  xhr.onload = () => {
    if(xhr.response != ''){
      var stats = JSON.parse(xhr.response)
      stats.gamesTotal = stats.gamesWin+stats.gamesLose+stats.gamesDraw
      stats.gamesWinPercentage = stats.gamesWin/Math.max(1, stats.gamesTotal)
      stats.averageGameTime = stats.totalGameTime/Math.max(1, stats.gamesTotal)

      gameWinText.innerHTML = stats.gamesWin
      gameLoseText.innerHTML = stats.gamesLose
      gameDrawText.innerHTML = stats.gamesDraw
      gameTotalText.innerHTML = stats.gamesTotal
      gameWinPercentageText.innerHTML = (stats.gamesWinPercentage * 100).toFixed(2)
      gameTotalTimeText.innerHTML = getTimeString(stats.totalGameTime)
      gameAverageTimeText.innerHTML = getTimeString(stats.averageGameTime)

      loadCount--; if(loadCount === 0) callback()
    }
  }
  xhr.send()
}

function loadUserSettings(callback){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/currentUserSettings')
  xhr.onload = () => {
    if(xhr.response != ''){
      settings = JSON.parse(xhr.response)
      for (var i = 0; i < boardStyleRadio.length; i++) {
        boardStyleRadio[i].checked = boardStyleRadio[i].value === settings.boardStyle
      }
      for (var i = 0; i < figureStyleRadio.length; i++) {
        figureStyleRadio[i].checked = figureStyleRadio[i].value === settings.figureStyle
      }
    }

    loadCount--; if(loadCount === 0) callback()
  }
  xhr.send()
}

function updateBoardStyle(){
  if(settings != null){
    settings.boardStyle = getCheckedValue("board-style-radio")
    updateUserSettings()
  }
}

function updateFigureStyle(){
  if(settings != null){
    settings.figureStyle = getCheckedValue("figure-style-radio")
    updateUserSettings()
  }
}

function updateUserSettings(){
  if(settings != null){
    var xhr = new XMLHttpRequest()
    xhr.open('PUT', '/data/currentUserSettings')
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8")
    xhr.send(JSON.stringify(settings))
  }
}

function loadReplayPageCount(callback){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/myReplaysPageCount')
  xhr.onload = () => {
    if(xhr.response) replayPageCount = JSON.parse(xhr.response)
    loadCount--; if(loadCount === 0) callback()
  }
  xhr.send()
}

function loadReplayPage(){
  document.getElementById("profile").hidden = false

  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/myReplays?page='+replayPageNum)
  xhr.onload = () => {
    if(xhr.response){
      replayPage = JSON.parse(xhr.response)
      updateReplayTable()
    }
  }
  xhr.send()
}

function loadAll(callback){
  loadUserStats(callback)
  loadUserSettings(callback)
  loadReplayPageCount(callback)
}

function nextReplayPage(){
  if(replayPageNum < replayPageCount-1){
    replayPageNum++
    loadReplayPage(replayPageNum)
  }
}

function prevReplayPage(){
  if(replayPageNum > 0){
    replayPageNum--
    loadReplayPage(replayPageNum)
  }
}

function startReplay(replayId){
  location.replace("/replay?id="+replayId)
}

function getReplayButtonHTML(replay){
  return '<button class="btn" onclick="startReplay('+"'"+replay.id+"'"+')">Посмотреть</button>'
}

function getReplayRowHTML(replay){
  var myColor = replay.whitePlayer === username ? "WHITE" : "BLACK"
  console.log(replay.whitePlayer)
  console.log(username)

  var trClass = ""
  if(replay.winner === "DRAW"){
    trClass = "class='replay-draw'"
  }else if(replay.winner === myColor){
    trClass = "class='replay-win'"
  }else{
    trClass = "class='replay-lose'"
  }

  var opponent = myColor === "WHITE" ? replay.blackPlayer : replay.whitePlayer
  var timeAgo = Date.parse(replayPage.currentTime)-Date.parse(replay.endDate)

  return "<tr><td "+trClass+">"+getTimeAgoString(timeAgo)
    +"</td><td "+trClass+">"+opponent
    +"</td><td "+trClass+">"+players[replay.settings.firstPlayer]
    +"</td><td "+trClass+">"+replay.settings.boardSize
    +"</td><td "+trClass+">"+arrangements[replay.settings.arrangement]
    +"</td><td "+trClass+">"+getReplayButtonHTML(replay)+"</td></tr>"
}

function updateReplayTable(){
  if(replayPage != null){
    replayPageNum = replayPage.page
    prevPageBtn.disabled = replayPageNum === 0
    nextPageBtn.disabled = replayPageNum >= replayPageCount-1
    pageNumText.innerHTML = (replayPageNum+1).toString() + " из " + Math.max(1, replayPageCount)
    if(replayPageCount != 0){
      replayTable.innerHTML = ""
      for(var i = 0; i < replayPage.replayList.length; i++){
          replayTable.innerHTML += getReplayRowHTML(replayPage.replayList[i])
      }
    }
  }
}

function submitAvatar(){
  if(avatarFile.files.length > 0){
    if(avatarFile.files[0].size >= 1048576){
      showMessage("Ошибка", "Размер загружаемого аватара не должен превышать 1 Мб")
    }else{
      var fileExtension = ['jpg']
      if (fileExtension.indexOf(avatarFile.files[0].name.split('.').pop().toLowerCase()) == -1) {
        showMessage("Ошибка", "Файл должен иметь расширение .jpg")
      }else{
        document.getElementById("floatingCirclesG").hidden = false
        document.getElementById("avatar-form").submit()
      }
    }
  }
}

var messageDialog = document.getElementById("message-dialog")
var messageHeader = document.getElementById("message-dialog-header")
var messageText = document.getElementById("message-dialog-text")
var closeMessageBtn = document.getElementById("close-message-btn")
closeMessageBtn.addEventListener("click", closeMessageDialog)

var username = document.getElementById('username').innerHTML
var settings = null

var gameWinText = document.getElementById('game-win')
var gameLoseText = document.getElementById('game-lose')
var gameDrawText = document.getElementById('game-draw')
var gameTotalText = document.getElementById('game-total')
var gameWinPercentageText = document.getElementById('game-win-percentage')
var gameTotalTimeText = document.getElementById('game-total-time')
var gameAverageTimeText = document.getElementById('game-average-time')
var avatarFile = document.getElementById('avatar-file')
avatarFile.addEventListener('change', submitAvatar)

var boardStyleRadio = document.getElementsByName("board-style-radio")
for (var i = 0; i < boardStyleRadio.length; i++) {
  boardStyleRadio[i].addEventListener("click", updateBoardStyle)
}
var figureStyleRadio = document.getElementsByName("figure-style-radio")
for (var i = 0; i < figureStyleRadio.length; i++) {
  figureStyleRadio[i].addEventListener("click", updateFigureStyle)
}

var replayPage = null
var replayPageNum = 0
var replayPageCount = 0
var replayTable = document.getElementById('replay-table-body')
var prevPageBtn = document.getElementById('prev-page-btn')
prevPageBtn.addEventListener("click", prevReplayPage)
var nextPageBtn = document.getElementById('next-page-btn')
nextPageBtn.addEventListener("click", nextReplayPage)
var pageNumText = document.getElementById('page-number')

var players = {WHITE:"Белые", BLACK:"Черные"}
var arrangements = {NORMAL:"Обычная", WIDE:"Широкая"}

var loadCount = 3
loadAll(loadReplayPage)