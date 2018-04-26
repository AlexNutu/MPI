window.onload = function clickOnConversations(e) {

    // Add active class to the current conversation (highlight it)
    var header = document.getElementById("allConversations");
    var conversations = header.getElementsByClassName("contact");
    for (var i = 0; i < conversations.length; i++) {
        conversations[i].addEventListener("click", function() {
            var current = document.getElementsByClassName("contact");
            for(var j = 0; j < current.length; j++){
                current[j].className = current[j].className.replace(" active", "");
            }
            this.className += " active";
        });
    }

}
