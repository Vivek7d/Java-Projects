<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Banking App - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        html, body {
            height: 100%;
            min-height: 100vh;
            margin: 0;
            padding: 0;
        }
        body {
            background: linear-gradient(135deg, #e3f0ff 0%, #fafcff 100%);
            font-family: 'Segoe UI', 'Roboto', Arial, sans-serif;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }
        .center-container {
            width: 100vw;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }
        .login-wrapper {
            max-width: 430px;
            width: 100%;
            background: #fff;
            border-radius: 28px;
            box-shadow: 0 4px 32px 0 #0d6efd16;
            padding: 42px 40px 32px 40px;
            margin-bottom: 28px;
        }
        .login-header {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 32px;
            justify-content: center;
        }
        .login-header i {
            font-size: 2.6rem;
            color: #0d6efd;
        }
        .login-header h2 {
            font-weight: 800;
            font-size: 2.1rem;
            margin: 0;
            color: #212529;
            letter-spacing: 0.02em;
        }
        .form-label {
            font-weight: 500;
            color: #222;
            margin-bottom: 4px;
        }
        .form-control, .form-select {
            border-radius: 12px;
            border: 1.2px solid #e3e7ed;
            height: 46px;
            font-size: 1.07rem;
            background: #f5f8fd;
            transition: border .2s;
        }
        .form-control:focus, .form-select:focus {
            border-color: #0d6efd;
            background: #fff;
            box-shadow: 0 0 0 2px #0d6efd22;
        }
        .login-btn {
            background: linear-gradient(90deg, #0d6efd 70%, #1e90ff 100%);
            color: #fff;
            border-radius: 12px;
            border: none;
            padding: 12px 0;
            font-weight: 700;
            font-size: 1.14rem;
            letter-spacing: 0.01em;
            margin-top: 10px;
            transition: background .15s;
        }
        .login-btn:hover, .login-btn:focus {
            background: linear-gradient(90deg, #1e90ff 95%, #0d6efd 100%);
            color: #fff;
        }
        .error {
            color: #dc3545;
            text-align: center;
            margin-bottom: 1rem;
            font-size: 1.08rem;
            font-weight: 500;
        }
        .toast-notification {
            position: fixed;
            top: 24px;
            right: 24px;
            color: white;
            padding: 13px 20px;
            border-radius: 9px;
            z-index: 1050;
            box-shadow: 0 4px 18px rgba(0,0,0,0.17);
            opacity: 0;
            transition: opacity 0.5s;
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 15px;
        }
        .toast-notification.show {
            opacity: 1;
        }
        .toast-notification.error {
            background-color: #dc3545;
        }
        .toast-notification.info {
            background-color: #0d6efd;
        }
        .bank-footer {
            text-align: center;
            margin-top: 0;
            color: #aaa;
            font-size: 1.01rem;
            letter-spacing: 0.02em;
            width: 100%;
        }
        @media (max-width: 600px) {
            .login-wrapper {
                padding: 22px 6vw 18px 6vw;
            }
            .login-header h2 {
                font-size: 1.3rem;
            }
        }
    </style>
</head>
<body>
    <div id="toast" class="toast-notification"></div>
    <div class="center-container">
        <div class="login-wrapper">
            <div class="login-header">
                <i class="bi bi-bank2"></i>
                <h2>Bank Login</h2>
            </div>
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <form action="auth" method="post" autocomplete="off">
                <input type="hidden" name="action" value="login">
                <div class="mb-3">
                    <label for="email" class="form-label">Username (Email)</label>
                    <input type="email" id="email" name="email" class="form-control" placeholder="Enter your email" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Enter your password" required>
                </div>
                <div class="mb-2">
                    <label for="role" class="form-label">Login as</label>
                    <select id="role" name="role" class="form-select">
                        <option value="Customer">Customer</option>
                        <option value="Admin">Admin</option>
                    </select>
                </div>
                <button type="submit" class="login-btn w-100"><i class="bi bi-box-arrow-in-right me-1"></i>Login</button>
            </form>
        </div>
        <div class="bank-footer">
            &copy; <script>document.write(new Date().getFullYear())</script> Modern Bank. All Rights Reserved.
        </div>
    </div>
    <c:if test="${not empty deactivated_error}">
        <script>
            (function() {
                const toastElement = document.getElementById('toast');
                const message = "${deactivated_error}";
                if (toastElement && message) {
                    toastElement.innerHTML = '<i class="bi bi-x-circle-fill"></i> ' + message;
                    toastElement.classList.add('error');
                    toastElement.classList.add('show');
                    setTimeout(() => toastElement.classList.remove('show'), 4000);
                }
            })();
        </script>
    </c:if>
    <c:if test="${not empty sessionScope.logout_toast}">
        <script>
            (function() {
                const toastElement = document.getElementById('toast');
                const message = "${sessionScope.logout_toast}";
                if (toastElement && message) {
                    toastElement.innerHTML = '<i class="bi bi-check-circle-fill"></i> ' + message;
                    toastElement.classList.add('info');
                    toastElement.classList.add('show');
                    setTimeout(() => toastElement.classList.remove('show'), 4000);
                }
            })();
        </script>
        <c:remove var="logout_toast" scope="session" />
    </c:if>
</body>
</html>