document.addEventListener("DOMContentLoaded", function() {
	
	let deleteBtns = document.querySelectorAll(".deleteUser");
	if (deleteBtns.length >= 1){
		deleteBtns.forEach(function(btn, i){
			btn.addEventListener("click", function(event){
				deleteUser(event.target);
			})
		})
	}
	
	let searchForm = document.querySelector("#searchForm");
	if(searchForm != null){
		searchForm.addEventListener("submit", function(event){
			event.preventDefault();
			let dataToFind = event.target.parentElement.parentElement.querySelector("#searchUser").value;
			findUser(dataToFind);
		});		
	}
	
	let cleanBtn = document.querySelector("#cleanBtn");
	if(cleanBtn != null){
		cleanBtn.addEventListener("click", function(event){
			let token = sessionStorage.getItem("token");
			let userId = sessionStorage.getItem("userId");
			cleanSearch(token, userId);
		})		
	}
	
	let addUserForm = document.querySelector("#addUserForm");
	if(addUserForm != null){
		addUserForm.addEventListener("submit", function(event){
			event.preventDefault();
			let name = document.querySelector("#name").value;
			let email = document.querySelector("#email").value;
			let password = document.querySelector("#password").value;
			
			let selected = document.querySelectorAll("#roles option:checked");
			let roles = Array.from(selected).map((option) => {
				let role = {
					id : option.value,
					name : option.text
				}
				return role;
			});
			if(name == "" || email == "" || password == ""){
				document.querySelector("#request-responses").classList.contains("alert-success") ? document.querySelector("#request-responses").classList.remove("alert-success") : '';
				document.querySelector("#request-responses").classList.add("alert-danger");
				document.querySelector("#request-responses").innerHTML = "Please, fill in all fields";
			} else {
				createUser(name, email, password, roles);			
			}
		})
	}
	
	if(window.location.href.includes("/api")){
		document.querySelector("#logOut").addEventListener("click", function(event){
			sessionStorage.clear();
			window.location.href = "/login?logOut";
		})		
	}
	
	if(window.location.href.includes("/login")){
		
		if(window.location.href.includes("logOut")) sessionStorage.clear();
	
		document.querySelector("#loginForm").addEventListener("submit", function(event){
			event.preventDefault();
			validateLogin();
		});
	}
	
	
});

function cleanSearch(token, userId){
	window.location.href = "/api/user/" + userId + "/users?token=" + token;
}