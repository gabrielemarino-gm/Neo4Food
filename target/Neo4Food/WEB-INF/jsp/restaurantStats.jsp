<%--
  Created by IntelliJ IDEA.
  User: gabriele
  Date: 12/02/23
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.*" %>
<%@ page import="org.apache.taglibs.standard.lang.jstl.NullLiteral" %>
<%@ page import="java.text.DecimalFormat" %>
<html>
<head>
<%
    ListDTO<AnalyticsDTO> bestOrari = (ListDTO<AnalyticsDTO>) request.getAttribute("Orari");
    ListDTO<AnalyticsDTO> bestPiatto = (ListDTO<AnalyticsDTO>) request.getAttribute("Piatto");
    AnalyticsDTO fatturatoGiornaliero = (AnalyticsDTO) request.getAttribute("Fatturato");
    ListDTO<AnalyticsDTO> modaPiatti = (ListDTO<AnalyticsDTO>) request.getAttribute("Moda");
%>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Restaurant Statistics</title>
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
</head>

<body>
    <%@include file="template/header.jsp"%>

    <div class="flex flex-wrap justify-center mt-4">
        <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 relative shadow-md">
            <h1 class="font-bold">Busiest Time</h1>
            <p>Top 4 busy hours of the day:</p>
<%
            if(bestOrari.getList() != null)
            {
                int i = 1;
                for (AnalyticsDTO ora: bestOrari.getList())
                {
%>
                    <div>
                        <%=i%>)&nbsp;<%=ora.getOrario()%>:00/<%=(Integer.parseInt(ora.getOrario())==0 ? "1" : Integer.parseInt(ora.getOrario())+1)%>:00
                    </div>
<%                  i++;
                }
            }
%>
        </div>

        <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 relative shadow-md">
            <h1 class="font-bold">Best Month's Dishes</h1>
<%
                if(bestPiatto.getList() != null)
                {
                    for (AnalyticsDTO piatto: bestPiatto.getList())
                    {
%>
                        <div>
                            <%=piatto.getDish()%>
                        </div>
                        <div>
                            Total solds:&nbsp;
                            <%=piatto.getCount()%>
                        </div>
<%                   }
                }
%>
        </div>

        <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 relative shadow-md">
            <h1 class="font-bold">Daily Revenue</h1>

<%
                if(fatturatoGiornaliero.getDouble() != 0.00)
                {
                    DecimalFormat df = new DecimalFormat("#.##");
%>
                    <div>
                        <%=df.format(fatturatoGiornaliero.getDouble())%>
                        &nbsp;
                        <%=fatturatoGiornaliero.getCurrency()%>
                    </div>
<%              }
%>
        </div>

        <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 relative shadow-md">
            <h1 class="font-bold">Mode Orders</h1>
<%
                if(modaPiatti.getList() != null)
                {
                    for (AnalyticsDTO moda: modaPiatti.getList())
                    {
%>
                        <div>
                            <%=moda.getDish()%>
                        </div>
<%
                    }
                }
%>
        </div>


    </div>


    <%@include file="template/footer.jsp"%>
</body>
</html>
