<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h4 class="mb-4">Add New Customer</h4>

<form id="addCustomerForm" action="admin" method="post" class="col-lg-6 col-md-8 needs-validation" novalidate>
    <input type="hidden" name="action" value="addCustomer">

    <div class="mb-3">
        <label for="firstName" class="form-label">First Name</label>
        <%-- Add pattern attribute and repopulate value on error --%>
        <input type="text" id="firstName" name="firstName" class="form-control" value="${firstName}" required pattern="[A-Za-z\\s]+">
        <div class="invalid-feedback">Please enter a valid first name (letters and spaces only).</div>
    </div>

    <div class="mb-3">
        <label for="lastName" class="form-label">Last Name</label>
        <input type="text" id="lastName" name="lastName" class="form-control" value="${lastName}" required pattern="[A-Za-z\\s]+">
        <div class="invalid-feedback">Please enter a valid last name (letters and spaces only).</div>
    </div>

    <div class="mb-3">
        <label for="email" class="form-label">Email Address</label>
        <input type="email" id="email" name="email" class="form-control" value="${email}" required>
        <div class="invalid-feedback">Please enter a valid email address.</div>
    </div>

    <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <input type="password" id="password" name="password" class="form-control" required>
        <div class="invalid-feedback">Please enter a password.</div>
    </div>

    <button type="submit" class="btn btn-primary">
        <i class="bi bi-person-plus me-2"></i>Submit
    </button>
</form>

<script>
(function () {
  'use strict'

  const form = document.getElementById('addCustomerForm');

  form.addEventListener('submit', function (event) {
    if (!form.checkValidity()) {
      event.preventDefault();
      event.stopPropagation();
    }
    
    form.classList.add('was-validated');
  }, false);

  const firstNameInput = document.getElementById('firstName');
  const lastNameInput = document.getElementById('lastName');
  const nameRegex = /^[A-Za-z\\s]+$/;

  function validateNameField(input) {
      if (nameRegex.test(input.value)) {
          input.setCustomValidity(''); 
      } else {
          input.setCustomValidity('Invalid name'); 
      }
  }

  firstNameInput.addEventListener('input', () => validateNameField(firstNameInput));
  lastNameInput.addEventListener('input', () => validateNameField(lastNameInput));

})();
</script>