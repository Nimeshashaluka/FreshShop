var address;

async function loadCheckOut() {

    const response = await fetch(
            "LoadCheckOut"
            );

    if (response.ok) {
        const json = await response.json();
        const popup = Notification();

        console.log(json);

        if (json.success) {
            //Store Response Data
            address = json.address;
            const cityList = json.cityList;
            const cartList = json.cartList;
            
            //Load Citys
            let citySelect = document.getElementById("city");
            citySelect.length =1;
            
            cityList.forEach(city =>{
                let cityOption = document.createElement("option");
                cityOption.value =city.id;
                cityOption.innerHTML =city.name;
                citySelect.appendChild(cityOption);
            });
            
            //Check Box save Address
            let currentAddressBox = document.getElementById("same-address");
            currentAddressBox.addEventListener("change", e =>{
                console.log("ok");
            });
            
        } else {
            window.location = "signIn.html";
        }

    }

}