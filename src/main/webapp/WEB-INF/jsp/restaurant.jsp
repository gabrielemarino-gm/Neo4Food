<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.model.Dish" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Restaurant Page</title>
  <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script type="text/javascript">

        function clearDOM(target)
        {
            while(target.hasChildNodes())
            {
                clearDOM(target.firstChild);
                target.removeChild(target.firstChild);
            }
        }

        function removeProduct(idTarget)
        {
            target = document.getElementById(idTarget);
            quantity = target.childNodes[1].childNodes[1];
            price = target.childNodes[0].childNodes[1];
            total = document.getElementById("total");

            total.innerText = (Math.round((parseFloat(total.innerText) - parseFloat(price.innerText))*100)/100).toFixed(2);
            quantity.innerText = parseInt(quantity.innerText) - 1;
            if(parseInt(quantity.innerText) == 0)
            {
                clearDOM(target);
                target.remove();
            }
        }

        // Aggiunge un Piatto al tuo ordine visualizzato a schermo
        function addProduct(idDish, name, cost)
        {
            // Prendi il form che forma gli ordini
            root = document.getElementById("ordini");

            // L'incremental serve per contare gli ordini aggiunti
            inc = document.getElementById("incremental");

            // Controllo se il Piatto e presente fra quelli disponibili nella pagina
            if(!isPresent(idDish))
            {
                // Creo i campi che rappresentano il piatto dentro l'ordine
                orderBoxDiv = document.createElement("div");
                detailsDiv = document.createElement("div");
                // pIdInput = document.createElement("input");
                titleInput = document.createElement("input");
                priceInput = document.createElement("input");
                buttonsDiv = document.createElement("div");
                removeButton = document.createElement("button");
                quantityInput = document.createElement("input");
                addButton = document.createElement("button");

                orderBoxDiv.id = idDish;
                titleInput.id = "name";
                titleInput.innerText = name;
                priceInput.id = "price";
                priceInput.innerText = parseFloat(cost).toFixed(2); // Aggiungo il prezzo nel formato XX.XX

                // Bottone meno, per decrementare o eliminare il prodotto aggiunto
                removeButton.id = "remove";
                removeButton.innerText = "---";
                removeButton.onclick = function(){ removeProduct(idDish)};

                // Quantita' attuale
                quantityInput.id = "quantity";
                quantityInput.innerText = 1;

                // Bottene piu, per aggiungere altri prodotti
                addButton.id = "add";
                addButton.innerText = "+++";
                addButton.onclick = function (){ addProduct(idDish, name)};

                root.appendChild(orderBoxDiv);
                orderBoxDiv.appendChild(detailsDiv);
                detailsDiv.appendChild(titleInput);
                detailsDiv.appendChild(priceInput);
                orderBoxDiv.appendChild(buttonsDiv);
                buttonsDiv.appendChild(removeButton);
                buttonsDiv.appendChild(quantityInput);
                buttonsDiv.appendChild(addButton);

                total = document.getElementById("total");
                total.innerText = (Math.round((parseFloat(total.innerText) + parseFloat(price.innerText))*100)/100).toFixed(2);

                inc.nodeValue = parseInt(inc.nodeValue) + 1;
            }
            else
            {
                orderBoxDiv = document.getElementById(idDish);

                total = document.getElementById("total");
                price = orderBoxDiv.childNodes[0].childNodes[1];
                quantity = orderBoxDiv.childNodes[1].childNodes[1];

                total.innerText = (Math.round((parseFloat(total.innerText) + parseFloat(price.innerText))*100)/100).toFixed(2);
                quantity.innerText = parseInt(quantity.innerText) + 1;
            }
        }

        function isPresent(id)
        {
            return document.getElementById(id);
        }

    </script>
  </head>
<body>
  <%
        RestaurantDTO details = (RestaurantDTO) request.getAttribute("details");
        ListDTO<Dish> list = details.getDishes();
  %>
<%--                Header con login o nomeutente--%>
    <%@include file="template/header.jsp"%>
      <div class="-top-6 overflow-hidden h-48 z-50">
          <img class="blur-md w-full" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood">
      </div>

        <div class="relative mx-auto w-2/3 rounded-lg bg-principale text-center py-3 -my-11 shadow-md px-5">
<%--        Restaurant detailed infos--%>
            <div class="text-3xl font-bold"><%= details.getName() %></div>
            <div class="h-5"></div>
            <div class="flex flex-wrap">
                <%
                    Float rate = details.getRating();
                    int rateInt = rate.intValue();
                    int nStar=0;
                    for (; nStar<rateInt; nStar++)
                    {
                %>
                        <img class="h-5" src="img/star.png" alt="star">
                <%
                    }

                    rate = rate*10;
                    nStar = rateInt;
                    if (rate%10 > 5)
                    {
                %>
                        <img class="h-5" src="img/half_star.png" alt="star">

                <%
                        nStar = rateInt+1;
                    }

                    for (; nStar<5; nStar++)
                    {
                %>
                    <img class="h-5" src="img/empty_star.png" alt="star">
                <%
                    }
                %>

                <div class="ml-auto flex flex-wrap">
                    <%
                        String money = details.getPricerange();
                        String[] splits = money.split("");
                        for (nStar=0; nStar<splits.length; nStar++)
                        {
                    %>
                            <img class="h-5" src="img/money.png" alt="star">
                    <%
                        }
                    %>
                </div>
            </div>
        </div>

        <div class="flex flex-wrap justify-end">
            <div class="flex flex-wrap my-16 w-3/4 mr-20">
<%--            List of available dishes--%>
                <% for(Dish i: list.getList())
                { 
                    String price = i.getCost() == 0.0 ? "-.-": i.getCost().toString(); %>
                    <div class="bg-principale rounded-xl w-1/3 mt-5 mr-5 text-center px-5 py-3 relative shadow-md">
                        <div class="text-xl font-bold"><%= i.getName()%></div>
                        <div class="h-3"></div>
                        <div class="text-left"><%= i.getDescription()%></div>
                        <div class="h-10"></div>
                        <div class="absolute bottom-3 left-4 font-bold"><%= price %> <%= i.getCurrency() %></div>
                        <button class="absolute bottom-3 right-4" onclick="addProduct('<%= i.getId() %>','<%=i.getName().replaceAll("'","\\\\'") %>','<%= i.getCost() %>')">
                            <img class="h-6" src="img/plus.png" alt="plus">
                        </button>
                    </div>
                <% } %>
            </div>



            <div class="sticky top-10 mr-5 rounded-xl border w-1/4 my-20  px-3 py-3">

                <form id="ordini" method="post" action="<c:url value="/checkout"/>">
                    <input id="incremental" type="hidden" name="incremental" value="0">
                    <input type="hidden" name="action" value="checkout">

                    <div>
                        <div>Total: </div>
                        <div id="total">0.0</div>
                        <div>
    <%--                    Checkout button here--%>
                            <button type="submit">CHECKOUT</button>
                        </div>
                    </div>
                </form>

            </div>





        </div>

      </div>
   <%@include file="template/footer.jsp"%>
  </body>
</html>
