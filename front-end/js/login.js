import { authApi } from './api.js';
const errorEl = document.getElementById('error-msg');
const tabLogin = document.getElementById('tab-login');
const tabReg = document.getElementById('tab-register');
const secLogin = document.getElementById('section-login');
const secReg = document.getElementById('section-register');
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
function switchTab(tab) {
    const isLogin = tab === 'login';
    tabLogin.style.fontWeight = isLogin ? '700' : '400';
    tabReg.style.fontWeight = isLogin ? '400' : '700';
    secLogin.style.display = isLogin ? 'block' : 'none';
    secReg.style.display = isLogin ? 'none' : 'block';
    errorEl.classList.remove('show');
}
tabLogin.addEventListener('click', () => switchTab('login'));
tabReg.addEventListener('click', () => switchTab('register'));
function showError(msg) {
    errorEl.textContent = msg;
    errorEl.classList.add('show');
}
document.getElementById('btn-login')?.addEventListener('click', async () => {
    const email = document.getElementById('login-email').value.trim();
    const password = document.getElementById('login-password').value;
    if (!email || !password)
        return showError('Completa todos los campos');
    if (!EMAIL_REGEX.test(email))
        return showError('El correo debe tener el formato usuario@dominio.com');
    try {
        const res = await authApi.login(email, password);
        localStorage.setItem('token', res.token);
        localStorage.setItem('role', res.role);
        window.location.href = res.role === 'ADMIN' ? '/admin.html' : '/index.html';
    }
    catch {
        showError('Email o contrasena incorrectos');
    }
});
document.getElementById('btn-register')?.addEventListener('click', async () => {
    const username = document.getElementById('reg-username').value.trim();
    const email = document.getElementById('reg-email').value.trim();
    const password = document.getElementById('reg-password').value;
    if (!username || !email || !password)
        return showError('Completa todos los campos');
    if (!EMAIL_REGEX.test(email))
        return showError('El correo debe tener el formato usuario@dominio.com');
    try {
        const res = await authApi.register(username, email, password);
        localStorage.setItem('token', res.token);
        localStorage.setItem('role', res.role);
        localStorage.setItem('username', username);
        window.location.href = '/index.html';
    }
    catch (e) {
        const msg = e instanceof Error ? e.message : 'No se pudo crear la cuenta';
        showError(msg);
    }
});
