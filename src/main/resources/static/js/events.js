document.addEventListener("DOMContentLoaded", function() {
	
	let deleteBtns = document.querySelectorAll(".deleteUser");
	//is admin
	if (deleteBtns.length >= 1){
		deleteBtns.forEach(function(btn, i){
			btn.addEventListener("click", function(event){
				deleteUser(event.target);
			})
		})
		
		document.querySelector("#searchForm").addEventListener("submit", function(event){
			event.preventDefault()
			let dataToFind = event.target.parentElement.parentElement.querySelector("#searchUser").value;
			console.log(dataToFind);
			findUser(dataToFind);
		});
		
		document.querySelector("#cleanBtn").addEventListener("click", function(event){
			let token = sessionStorage.getItem("token");
			let userId = sessionStorage.getItem("userId");
			cleanSearch(token, userId);
		})
	}
	
	if(window.location.href.includes("/api")){
		document.querySelector("#logOut").addEventListener("click", function(event){
			sessionStorage.clear();
			window.location.href = "/login?logOut";
		})		
	}
	
	//check logOut to clean sessionStorage
	if(window.location.href.includes("/login")){
		
		if(window.location.href.includes("logOut")) sessionStorage.clear();
	
		document.querySelector("#loginForm").addEventListener("submit", function(event){
			validateLogin();
		});
	}
	
	
});

function cleanSearch(token, userId){
	let host = window.location.hostname;
	console.log(window.location.hostname)
	console.log(window.location.host)
	window.location.href = "/api/user/" + userId + "/users?token=" + token;
}