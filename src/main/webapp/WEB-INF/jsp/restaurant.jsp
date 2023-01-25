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
            inc = document.getElementById("incremental");
            if(!isPresent(idn)){


                orderBox = document.createElement("div");
                    details = document.createElement("div");
                        pId = document.createElement("input");
                        title = document.createElement("input");
                        price = document.createElement("input");
                    buttons = document.createElement("div");
                        remove = document.createElement("div");
                        quantity = document.createElement("input");
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

                inc.nodeValue = parseInt(inc.nodeValue) + 1;
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

                    for (; nStar<10; nStar++)
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
                        <div class="absolute bottom-1 right-2"><%= price %> <%= i.getCurrency() %></div>
                        <button onclick = "addProduct('<%= i.getId() %>','<%=i.getName().replaceAll("'","\\\\'") %>','<%= i.getCost() %>')"> + </button>
                    </div>
                <% } %>
            </div>
            <div class="fixed mr-5 rounded-xl border h-80 w-1/4 my-20">













                <form id="ordini" method="post" action="<c:url value="/checkout"/>">
                    <input id="incremental" type="hidden" name="incremental" value="0">
                    <input type="hidden" name="action" value="checkout">

                    <div>
                        <div>Total: </div>
                        <div id="total">0.0</div>
                        <div>
    <%--                        Checkout button here--%>
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
