<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>


<li><a href='#'><span>${category.categoryName}</span></a>
	<c:choose>
		<c:when test="${not empty category.categoryVos}">
			<c:forEach items="${category.categoryVos}" var="subCategory" varStatus="subcatstatus">
				<c:set var="category" value="${subCategory}" scope="request" />
           		<jsp:include page="menulist.jsp" />
			</c:forEach>
		</c:when>
	</c:choose>
</li>