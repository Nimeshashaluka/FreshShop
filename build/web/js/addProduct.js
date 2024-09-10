var modelList;

async function loadData() {

    const response = await fetch(
            "LoadDatas",
            );

    if (response.ok) {
        const json = await response.json();
//        const popup = Notification();

//        console.log(json);

        const categoryList = json.categoryList;
        modelList = json.modelList;
        const weightList = json.weightList;

        loadSelect("categorySelect", categoryList, "name");
        loadSelect("modelSelect", modelList, "name");
        loadSelect("weightSelect", weightList, "name");

    } else {
//        popup.error({
//
//            message: "Please try again later!"
//        });
    }
}

function loadSelect(selectTagId, list, property) {
    const selectTag = document.getElementById(selectTagId);
    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item.id;
        optionTag.innerHTML = item[property];
        selectTag.appendChild(optionTag);

    });
}

function updateModels() {

    let modelSelectTag = document.getElementById("modelSelect");
    let selectedcategoryId = document.getElementById("categorySelect").value;
    modelSelectTag.length = 1;


    modelList.forEach(model => {
        if (model.category.id == selectedcategoryId) {
            let optionTag = document.createElement("option");
            optionTag.value = model.id;
            optionTag.innerHTML = model.name;
            modelSelectTag.appendChild(optionTag);
        }
    });

}