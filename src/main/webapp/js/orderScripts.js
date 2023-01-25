function clearDOM(target){
    while(target.hasChildNodes()){
        clearDOM(target.firstChild);
        target.removeChild(target.firstChild);
    }
}

function removeProduct(idn){
    target = document.getElementById(idn);
    quantity = target.childNodes[1].childNodes[1];
    price = target.childNodes[0].childNodes[1];
    total = document.getElementById("total");

    total.innerText = (Math.round((parseFloat(total.innerText) - parseFloat(price.innerText))*100)/100).toFixed(2);
    quantity.innerText = parseInt(quantity.innerText) - 1;
    if(parseInt(quantity.innerText) == 0) {
        clearDOM(target);
        target.remove();
    }
}

function addProduct(idn, name, cost){
    root = document.getElementById("ordini");
    if(!isPresent(idn)){

        orderBox = document.createElement("div");
        details = document.createElement("div");
        title = document.createElement("div");
        price = document.createElement("div");
        buttons = document.createElement("div");
        remove = document.createElement("div");
        quantity = document.createElement("div");
        add = document.createElement("div");

        orderBox.id = idn;

        title.id = "name";
        title.innerText = name;
        price.id = "price";
        price.innerText = parseFloat(cost).toFixed(2);

        remove.id = "remove";
        remove.innerText = "-";
        remove.onclick = function(){ removeProduct(idn)};
        quantity.id = "quantity";
        quantity.innerText = 1;
        add.id = "add";
        add.innerText = "+";
        add.onclick = function (){ addProduct(idn, name)};

        root.appendChild(orderBox);
        orderBox.appendChild(details);
        details.appendChild(title);
        details.appendChild(price);
        orderBox.appendChild(buttons);
        buttons.appendChild(remove);
        buttons.appendChild(quantity);
        buttons.appendChild(add);

        total = document.getElementById("total");
        total.innerText = (Math.round((parseFloat(total.innerText) + parseFloat(price.innerText))*100)/100).toFixed(2);

    }else{
        orderBox = document.getElementById(idn);

        total = document.getElementById("total");
        price = orderBox.childNodes[0].childNodes[1];
        quantity = orderBox.childNodes[1].childNodes[1];

        total.innerText = (Math.round((parseFloat(total.innerText) + parseFloat(price.innerText))*100)/100).toFixed(2);
        quantity.innerText = parseInt(quantity.innerText) + 1;
    }
}

function isPresent(id){
    return document.getElementById(id);
}