<!--
  ** Welcome screen
  ** @author Matthias L. Jugel
  ** @version $Id$
  -->

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
  <tr class="table-header">
    <td width="100%">User name</td><td>Email</td><td>Roles</td><td>Status</td><td colspan="2">Action</td>
  </tr>
  <c:forEach items="${usermanager.all}" var="user" varStatus="idx">
    <tr class="table-<c:out value='${idx.count mod 2}'/>">
      <td><b><i><c:out value="${user.login}"/></i></b></td>
      <td>
        <c:if test="${user.email != null}">
          <a href="mailto:<c:out value="${user.email}"/>"><c:out value="${user.email}"/></a>
        </c:if>
      </td>
      <td><nobr><c:out value="${user.roles}"/></nobr></td>
      <td><nobr><c:out value="${user.status}"/></nobr></td>
      <td>
        <form method="POST" action="<c:url value='/exec/admin/user'/>">
          <input type="hidden" name="command" value="edit">
          <input type="hidden" name="login" value="<c:out value='${user.login}'/>">
          <input type="submit" name="ok" value="Edit">
        </form>
      </td>
      <td>
        <c:if test="${config.adminLogin != user.login}">
          <form method="POST" action="<c:url value='/exec/admin/user'/>">
            <input type="hidden" name="command" value="remove">
            <input type="hidden" name="login" value="<c:out value='${user.login}'/>">
            <input style="color: red" type="submit" name="ok" value="Remove">
          </form>
        </c:if>
      </td>
    </tr>
  </c:forEach>
</table>
