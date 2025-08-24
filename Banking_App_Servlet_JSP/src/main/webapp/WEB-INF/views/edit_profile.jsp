<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
<div class="container py-5">
    <div class="profile-edit-wrapper mx-auto" style="max-width: 550px;">
        <h3 class="mb-4 text-primary d-flex align-items-center" style="font-weight: 700;">
            <i class="bi bi-person-lines-fill me-2"></i>Edit Your Profile
        </h3>

        <!-- Toast for error messages -->
        <div aria-live="polite" aria-atomic="true" class="position-relative">
            <div class="toast-container position-fixed bottom-0 end-0 p-3" id="toastPlacement">
                <div id="errorToast" 
                     class="toast align-items-center text-bg-danger border-0" 
                     role="alert" 
                     aria-live="assertive" 
                     aria-atomic="true"
                     style="min-width: 250px;" 
                     <c:if test="${empty error_toast}">hidden</c:if>>
                  <div class="d-flex">
                    <div class="toast-body">
                      ${error_toast}
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                  </div>
                </div>
            </div>
        </div>

        <form id="editProfileForm" action="customer" method="post" autocomplete="off" class="needs-validation" novalidate>
            <input type="hidden" name="action" value="updateProfile">

            <div class="row g-3 mb-3">
                <div class="col-md-6">
                    <label for="firstName" class="form-label">First Name</label>
                    <input type="text" id="firstName" name="firstName"
                           value="${not empty submittedFirstName ? submittedFirstName : sessionScope.user.firstName}"
                           class="form-control" required pattern="[A-Za-z\\s]+">
                    <div class="invalid-feedback">Letters and spaces only.</div>
                </div>
                <div class="col-md-6">
                    <label for="lastName" class="form-label">Last Name</label>
                    <input type="text" id="lastName" name="lastName"
                           value="${not empty submittedLastName ? submittedLastName : sessionScope.user.lastName}"
                           class="form-control" required pattern="[A-Za-z\\s]+">
                    <div class="invalid-feedback">Letters and spaces only.</div>
                </div>
            </div>

            <div class="mb-3">
                <label for="email" class="form-label">Email Address</label>
                <input type="email" id="email" name="email"
                       value="${not empty submittedEmail ? submittedEmail : sessionScope.user.email}"
                       class="form-control" required>
                <div class="invalid-feedback">Please enter a valid email.</div>
            </div>

            <div class="mb-4">
                <label for="password" class="form-label">New Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Leave blank to keep current password">
                <div class="form-text">Enter a new password only if you wish to change it.</div>
            </div>

            <div class="d-flex justify-content-end gap-2">
                <a href="${pageContext.request.contextPath}/customer?action=dashboard" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-success">
                    <i class="bi bi-save me-1"></i>Update Profile
                </button>
            </div>
        </form>
    </div>
</div>

<style>
.profile-edit-wrapper {
    background: #fff;
    border-radius: 18px;
    box-shadow: 0 2px 16px #0001;
    padding: 2rem 2.5rem 2rem 2.5rem;
}

@media (max-width: 600px) {
    .profile-edit-wrapper {
        padding: 1.2rem .5rem 1.5rem .5rem;
    }
}
</style>

<script>
(function () {
  'use strict'
  const form = document.getElementById('editProfileForm');

  form.addEventListener('submit', function (event) {
    if (!form.checkValidity()) {
      event.preventDefault();
      event.stopPropagation();
    }
    form.classList.add('was-validated');
  }, false);

  const firstNameInput = document.getElementById('firstName');
  const lastNameInput = document.getElementById('lastName');
  const nameRegex = /^[A-Za-z\s]+$/;

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

document.addEventListener('DOMContentLoaded', function() {
    var errorToast = document.getElementById('errorToast');
    if (errorToast && !errorToast.hasAttribute('hidden')) {
        var toast = new bootstrap.Toast(errorToast);
        toast.show();
    }
});
</script>
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>