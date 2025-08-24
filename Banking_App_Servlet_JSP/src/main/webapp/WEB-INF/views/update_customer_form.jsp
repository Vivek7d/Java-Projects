<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-lg-8 col-md-10">

            <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="text-primary d-flex align-items-center" style="font-weight: 700;">
                    <i class="bi bi-pencil-square me-2"></i>Edit Customer Details
                </h3>
                <a href="admin?action=listCustomers" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left me-1"></i>Back to Customer List
                </a>
            </div>

            <div class="card shadow-sm" style="border-radius: 16px;">
                <div class="card-body p-4">
                    <%-- Add an ID to the form for our JS --%>
                    <form id="updateCustomerForm" action="admin" method="post" autocomplete="off" novalidate>
                        <input type="hidden" name="action" value="updateCustomer">
                        <input type="hidden" name="id" value="${customer.id}">

                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label for="firstName" class="form-label">First Name</label>
                                <%-- Add a pattern for HTML5 validation --%>
                                <input type="text" class="form-control" id="firstName" name="firstName" value="${customer.firstName}" required pattern="[A-Za-z\\s]+">
                                <div class="invalid-feedback">Please enter a valid first name (letters and spaces only).</div>
                            </div>
                            <div class="col-md-6">
                                <label for="lastName" class="form-label">Last Name</label>
                                <input type="text" class="form-control" id="lastName" name="lastName" value="${customer.lastName}" required pattern="[A-Za-z\\s]+">
                                 <div class="invalid-feedback">Please enter a valid last name (letters and spaces only).</div>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">Email Address</label>
                            <input type="email" class="form-control" id="email" name="email" value="${customer.email}" required>
                             <div class="invalid-feedback">Please enter a valid email address.</div>
                        </div>
                        
                        <div class="mb-4">
                             <label for="accountNumber" class="form-label">Account Number</label>
                            <input type="text" class="form-control" id="accountNumber" name="accountNumber" value="${customer.accountNumber}" readonly disabled>
                        </div>

                        <hr class="my-4">

                        <div class="d-flex justify-content-end gap-2">
                            <a href="admin?action=listCustomers" class="btn btn-secondary">Cancel</a>
                            <button type="submit" class="btn btn-success">
                                <i class="bi bi-check-circle me-1"></i>Save Changes
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
(function () {
  'use strict'

  const form = document.getElementById('updateCustomerForm');

  form.addEventListener('submit', function (event) {
    // Check the form's validity using HTML5 constraints (required, pattern, type=email)
    if (!form.checkValidity()) {
      event.preventDefault(); 
      event.stopPropagation();
    }
    
    // Add Bootstrap's validation class to show feedback
    form.classList.add('was-validated');
  }, false);

  const firstNameInput = document.getElementById('firstName');
  const lastNameInput = document.getElementById('lastName');
  const nameRegex = /^[A-Za-z\\s]+$/;

  function validateNameField(input) {
      if (nameRegex.test(input.value)) {
          input.setCustomValidity(''); // Field is valid
      } else {
          input.setCustomValidity('Invalid name'); // Set as invalid so the feedback shows up
      }
  }

  firstNameInput.addEventListener('input', () => validateNameField(firstNameInput));
  lastNameInput.addEventListener('input', () => validateNameField(lastNameInput));

})();
</script>