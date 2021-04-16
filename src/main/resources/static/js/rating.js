function getTimeString(seconds){
  return Math.floor(seconds/3600).toString().padStart(2, '0') + ":"
  + Math.floor(seconds/60%60).toString().padStart(2, '0') + ":"
  + Math.floor(seconds%60).toString().padStart(2, '0')
}

function getCheckedValue(elementName){
  var rad = document.getElementsByName(elementName)
  for (var i = 0; i < rad.length; i++) {
    if (rad[i].checked) {
	    return rad[i].value
    }
  }
}

function updateRatingType(){
  ratingType = getCheckedValue("rating-type-radio")
  if(ratingType === "WINNERS"){
    ratingTypeHeader.innerHTML = "Количество побед"
  }else if(ratingType === "GAME_TIME"){
    ratingTypeHeader.innerHTML = "Общее время в игре"
  }
  updateRatingTable()
}

function getUserRowHTML(user, idx){
  var trClass = user.username === username ? "class='highlighted'" : ""
  var ratingText = ratingType === "GAME_TIME" ? getTimeString(user.rating) : user.rating
  return "<tr><td "+trClass+">"+idx
    +"</td><td "+trClass+"><img class='avatar_small' src='"+user.avatar+"'/>"
    +"</td><td "+trClass+">"+user.username
    +"</td><td "+trClass+">"+ratingText+"</td></tr>"
}

function updateRatingTable(){
  if(rating != null){
    curRating = rating[ratingType]
    ratingTable.innerHTML = ""
    for(var i = 0; i < curRating.length; i++){
      ratingTable.innerHTML += getUserRowHTML(curRating[i], i+1)
    }

    var place = curRating.length + "+"
    for(var i = 0; i < curRating.length; i++){
      if(username === curRating[i].username){
        place = i+1
      }
    }
    myRatingTxt.innerHTML = "Место в рейтинге: " + place
  }
}

function loadUsername(){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/currentUsername')
  xhr.onload = () => {
    username = xhr.response
    updateRatingTable()
  }
  xhr.send()
}

function loadRating(){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/rating')
  xhr.onload = () => {
    if(xhr.response) {
      rating = JSON.parse(xhr.response)
      updateRatingTable()
    }
  }
  xhr.send()
}

var username = null
loadUsername()

var ratingType = getCheckedValue("rating-type-radio")
var ratingTypeRadio = document.getElementsByName("rating-type-radio")
for (var i = 0; i < ratingTypeRadio.length; i++) {
  ratingTypeRadio[i].addEventListener("click", updateRatingType)
}

var myRatingTxt = document.getElementById("my-rating")
var ratingTable = document.getElementById("rating-table-body")
var ratingTypeHeader = document.getElementById("rating-type-header")
var rating = null
loadRating()