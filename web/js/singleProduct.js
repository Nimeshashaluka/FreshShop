async function loadProduct() {
    const parameters = new URLSearchParams(window.location.search);

    if (parameters.has("id")) {
        const productId = parameters.get("id");

        const response = await fetch("LoadSingleProduct?id=" + productId);

        if (response.ok) {

            const json = await response.json();
            console.log(json.product.id);

            const id = json.product.id;
            document.getElementById("image1").src = "productImage/" + id + "/image1.png";

            document.getElementById("pid").innerHTML = json.product.id;
            document.getElementById("productTitle").innerHTML = json.product.title;
            document.getElementById("price").innerHTML = "Rs. "+ new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
            ).format(json.product.price);
            document.getElementById("category").innerHTML = json.product.model.category.name;
            document.getElementById("discription").innerHTML = json.product.description;

                let ProductHtml = document.getElementById("similerProduct");
                document.getElementById("similerProductMain").innerHTML="";


            json.productList.forEach(item => {
                let productCloneHtml = ProductHtml.cloneNode(true);

                productCloneHtml.querySelector("#similerProduct-a").href = "singleProductView.html?id=" + item.id;
                productCloneHtml.querySelector("#similerProductImage").src = "productImage/" + item.id + "/image1.png";
                productCloneHtml.querySelector("#similerProductTitle").innerHTML = item.title;
                productCloneHtml.querySelector("#similerProductPrice").innerHTML ="Rs. "+  new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                ).format(item.price);



                document.getElementById("similerProductMain").appendChild(productCloneHtml);
            });

        } else {
            window.location = "index.html";
        }
    } else {
        window.location = "index.html";
    }

}