//var address;

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
            const address = json.address;
            const cityList = json.cityList;
            const cartList = json.cartList;

            //Load Citys
            let citySelect = document.getElementById("city");
            citySelect.length = 1;

            cityList.forEach(city => {
                let cityOption = document.createElement("option");
                cityOption.value = city.id;
                cityOption.innerHTML = city.name;
                citySelect.appendChild(cityOption);
            });

            //Check Box save Address
            let currentAddressBox = document.getElementById("same-address");
            currentAddressBox.addEventListener("change", e => {
//                console.log("ok");

                let first_name = document.getElementById("firstName");
                let last_name = document.getElementById("lastName");
                let address1 = document.getElementById("address");
                let address2 = document.getElementById("address2");
                let postal_Code = document.getElementById("postalCode");
                let mobile = document.getElementById("mobile");
                let city = document.getElementById("city");

                if (currentAddressBox.checked) {
                    first_name.value = address.first_name;
                    last_name.value = address.last_name;
                    address1.value = address.line1;
                    address2.value = address.line2;
                    postal_Code.value = address.postal_code;
                    mobile.value = address.mobile;
                    city.value = address.city.id;
                    city.disabled = true;

                } else {
                    first_name.value = "";
                    last_name.value = "";
                    address1.value = "";
                    address2.value = "";
                    postal_Code.value = "";
                    mobile.value = "";
                    city.value = 0;
                    city.disabled = false;

                }
            });

            //Load Cart Item - Ch = checkBox

            let chechBody = document.getElementById("checkBody");
            let checkBodyRow = document.getElementById("checkBodyRow");
            let checkShipping = document.getElementById("checkShipping");
            let checkSubTotal = document.getElementById("checkSubTotalM");

            chechBody.innerHTML = "";
            let subtotal = 0;

            cartList.forEach(item => {
                let checkItemClone = checkBodyRow.cloneNode(true);
                checkItemClone.querySelector("#checkItemT").innerHTML = item.product.title;
                checkItemClone.querySelector("#checkItemQty").innerHTML = item.product.qty;
                checkItemClone.querySelector("#checkItemPrice").innerHTML = "Rs . " + new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                ).format(item.product.price);

                let checkItemTotal = item.product.price * item.qty;

                subtotal += checkItemTotal;

                checkItemClone.querySelector("#checkItemTotal").innerHTML = "Rs . " + new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                ).format(checkItemTotal);
                chechBody.appendChild(checkItemClone);

            });

            let shippingAmo = subtotal;
            shippingAmo += 250;

            document.getElementById("checkSubTotal").innerHTML = "Rs . " + new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
            ).format(shippingAmo);



        } else {
            window.location = "signIn.html";
        }

    }

}