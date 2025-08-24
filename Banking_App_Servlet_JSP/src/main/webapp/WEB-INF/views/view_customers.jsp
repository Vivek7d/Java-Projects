<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h3 class="mb-4 text-center">All Customers</h3>

<div class="row mb-3 align-items-center">
    <!-- Search Input -->
    <div class="col-md-6">
        <div class="input-group">
            <span class="input-group-text"><i class="bi bi-search"></i></span>
            <input type="text" id="searchInput" class="form-control" placeholder="Search by Name or Account Number..." onkeyup="filterAndSearchTable()">
        </div>
    </div>
    <!-- Status Filter Radio Buttons -->
    <div class="col-md-6 d-flex justify-content-end">
        <div class="btn-group" role="group" aria-label="Status filter">
            <input type="radio" class="btn-check" name="statusFilter" id="filterAll" value="ALL" autocomplete="off" checked onchange="filterAndSearchTable()">
            <label class="btn btn-outline-primary" for="filterAll">All</label>

            <input type="radio" class="btn-check" name="statusFilter" id="filterActive" value="ACTIVE" autocomplete="off" onchange="filterAndSearchTable()">
            <label class="btn btn-outline-success" for="filterActive">Active</label>

            <input type="radio" class="btn-check" name="statusFilter" id="filterInactive" value="INACTIVE" autocomplete="off" onchange="filterAndSearchTable()">
            <label class="btn btn-outline-secondary" for="filterInactive">Inactive</label>
        </div>
    </div>
</div>

<div class="table-responsive">
    <table id="customersTable" class="table table-striped table-hover table-bordered align-middle shadow-sm">
        <thead class="table-dark">
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Account Number</th>
                <th>Balance</th>
                <th class="text-center">Status</th>
                <th class="text-center">Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="customer" items="${requestScope.customerList}">
                <%-- We add a data-status attribute to the row for easy JS filtering --%>
                <tr data-status="${customer.isActive() ? 'ACTIVE' : 'INACTIVE'}">
                    <td>${customer.firstName}</td>
                    <td>${customer.lastName}</td>
                    <td>${customer.accountNumber}</td>
                    <td>₹ ${customer.balance}</td>
                    <td class="text-center">
                        <c:if test="${customer.isActive()}">
                            <span class="badge bg-success">Active</span>
                        </c:if>
                        <c:if test="${!customer.isActive()}">
                            <span class="badge bg-secondary">Inactive</span>
                        </c:if>
                    </td>
                    <td class="text-center">
                        <c:if test="${customer.isActive()}">
                            <%-- Actions for ACTIVE users --%>
                            <a href="admin?action=showUpdateForm&id=${customer.id}" class="btn btn-sm btn-primary" title="Update"><i class="bi bi-pencil-square"></i></a>
                            <a href="admin?action=delete&id=${customer.id}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to deactivate this account?');" title="Deactivate"><i class="bi bi-trash"></i></a>
                        </c:if>
                        <c:if test="${!customer.isActive()}">
                             <%-- Action for INACTIVE users --%>
                            <a href="admin?action=reactivate&id=${customer.id}" class="btn btn-sm btn-success" onclick="return confirm('Are you sure you want to reactivate this account?');" title="Reactivate"><i class="bi bi-arrow-clockwise"></i></a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <tr id="noResultsRow" style="display:none;">
                <td colspan="6" class="text-center text-muted">No customers found matching your criteria.</td>
            </tr>
        </tbody>
    </table>
</div>

<script>
function filterAndSearchTable() {
    const searchValue = document.getElementById('searchInput').value.trim().toUpperCase();
    // Get the value of the selected radio button
    const statusFilter = document.querySelector('input[name="statusFilter"]:checked').value;
    
    const table = document.getElementById('customersTable');
    const rows = table.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    const noResultsRow = document.getElementById('noResultsRow');
    let visibleRowCount = 0;

    for (let i = 0; i < rows.length; i++) {
        if (rows[i].id === 'noResultsRow') continue;

        const firstName = rows[i].cells[0].textContent.toUpperCase();
        const lastName = rows[i].cells[1].textContent.toUpperCase();
        const accountNumber = rows[i].cells[2].textContent.toUpperCase();
        const rowStatus = rows[i].dataset.status; // Get status from data-status attribute

        const matchesSearch = 
            (firstName + " " + lastName).includes(searchValue) ||
            accountNumber.includes(searchValue);
            
        const matchesStatus = 
            (statusFilter === 'ALL') || 
            (rowStatus === statusFilter);

        if (matchesSearch && matchesStatus) {
            rows[i].style.display = "";
            visibleRowCount++;
        } else {
            rows[i].style.display = "none";
        }
    }
    
    noResultsRow.style.display = (visibleRowCount === 0) ? "" : "none";
}
</script>