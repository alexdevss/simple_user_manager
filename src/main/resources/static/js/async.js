async function deleteUser(btn){
	let row = btn.parentElement.parentElement;
	let userId = row.querySelector(".id-field").innerHTML;	
	let loggedToken = sessionStorage.getItem("token");
	 const settings = {
  	        method: 'DELETE',
  	        headers: {
  	            Accept: 'application/json',
  	            'Content-Type': 'application/json',
  	        },
  	        body: JSON.stringify({
  	        	'id' : userId,
  	        	'token' : loggedToken
  	        })
  	    };
  	    try {
  	        const fetchResponse = await fetch(`http://localhost:8080/api/user/${userId}`, settings);
  	        const data = await fetchResponse.json();
  			if(data.success){
  				row.remove();
  				document.querySelector("#request-responses").classList.contains("alert-danger") ? document.querySelector("#request-responses").classList.remove("alert-danger") : '';
				document.querySelector("#request-responses").classList.add("alert-success");
  				document.querySelector("#request-responses").innerHTML = data.message;
  			} else {
  				document.querySelector("#request-responses").classList.contains("alert-success") ? document.querySelector("#request-responses").classList.remove("alert-success") : '';
  				document.querySelector("#request-responses").classList.add("alert-danger");
  				document.querySelector("#request-responses").innerHTML = data.message;
  			}
  	    } catch (e) {
  	        console.log(e);
  	    }
}

async function findUser(id){
	const falseData = {};
	if(id == ""){
		falseData.success = false,
		falseData.message = "Please, type and ID"
		document.querySelector("#request-responses").classList.add("alert-danger");
		document.querySelector("#request-responses").innerHTML = falseData.message;
		return false;
	}
	let loggedToken = sessionStorage.getItem("token");
	let loggedId = sessionStorage.getItem("userId");
	const settings = {
  	        method: 'GET',
  	        headers: {
  	            Accept: 'application/json'
  	        }
  	    };		
		
  	    try {
  	        const fetchResponse = await fetch(`http://localhost:8080/api/user/${loggedId}/users/${id}?token=${loggedToken}`, settings);
  	        const data = await fetchResponse.json();
  			if(data.success){
				document.querySelectorAll("#usersTable tbody tr").forEach((row, i) => {
					if(row.getAttribute("data-id") == data.userToFind){
						row.style.display = "table-row"; 
					} else {
						row.style.display = "none"; 					
					}
				})
				document.querySelector("#request-responses").classList.contains("alert-danger") ? document.querySelector("#request-responses").classList.remove("alert-danger") : '';
				document.querySelector("#request-responses").classList.add("alert-success");
  				document.querySelector("#request-responses").innerHTML = data.message;
  			} else {
				document.querySelector("#request-responses").classList.contains("alert-success") ? document.querySelector("#request-responses").classList.remove("alert-success") : '';
  				document.querySelector("#request-responses").classList.add("alert-danger");
  				document.querySelector("#request-responses").innerHTML = data.message;
  			}
  	    } catch (e) {
			console.log(e)
  	    }
}
async function validateLogin(){
    const settings = {
        method: 'POST',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
        	'email' : document.querySelector("#userEmail").value,
        	'password' : document.querySelector("#userPassword").value
        })
    };
    try {
        const fetchResponse = await fetch("http://localhost:8080/validateLogin", settings);
        const data = await fetchResponse.json();
		if(data.success){
			sessionStorage.setItem("token", data.token);
			sessionStorage.setItem("userId", data.userId);
			sessionStorage.setItem("admin", data.isAdmin);
			
			window.location.href = "api/user/"+data.userId+"/users?token="+data.token;
		} else {
			window.location.href = "/login?error"
		}
    } catch (e) {

        console.log(e);
    }    
	
};

async function createUser(name, email, password, roles){
	let token = sessionStorage.getItem("token");
	const settings = {
		method: "POST",
		headers: {
			Accept: 'application/json',
            'Content-Type': 'application/json'
		},
		body : JSON.stringify({
			name : name,
			email : email,
			password : password,
			roles : roles,
			token: token
		})
	};
	try{
		
		let host = location.host;
		let currentUserId = sessionStorage.getItem("userId");
		let url = `http://${host}/api/user/${currentUserId}/addUser`;
		
		const fetchResponse = await fetch(url, settings);
		const data = await fetchResponse.json();
		
		if(data.success){
			document.querySelector("#request-responses").classList.contains("alert-danger") ? document.querySelector("#request-responses").classList.remove("alert-danger") : '';
			document.querySelector("#request-responses").classList.add("alert-success");
			document.querySelector("#request-responses").innerHTML = data.message;
			document.querySelector("#addUserForm").reset();
		} else {
			document.querySelector("#request-responses").classList.contains("alert-success") ? document.querySelector("#request-responses").classList.remove("alert-success") : '';
			document.querySelector("#request-responses").classList.add("alert-danger");
			document.querySelector("#request-responses").innerHTML = data.message;
		}
	}catch(e){
		console.log(e);
	}
}