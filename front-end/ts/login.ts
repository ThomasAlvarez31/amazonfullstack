import { authApi } from './api.js'

const tabLogin  = document.getElementById('tab-login')!
const tabReg    = document.getElementById('tab-register')!
const secLogin  = document.getElementById('section-login')!
const secReg    = document.getElementById('section-register')!
const errorEl   = document.getElementById('error-msg')!
const errorElReg = document.getElementById('error-msg-reg')!

const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

function switchTab(tab: 'login' | 'register'): void {
  const isLogin = tab === 'login'
  tabLogin.classList.toggle('active', isLogin)
  tabReg.classList.toggle('active', !isLogin)
  secLogin.style.display = isLogin ? 'block' : 'none'
  secReg.style.display   = isLogin ? 'none'  : 'block'
  errorEl.classList.remove('show')
  errorElReg.classList.remove('show')
}

tabLogin.addEventListener('click', () => switchTab('login'))
tabReg.addEventListener('click',   () => switchTab('register'))

document.getElementById('btn-login')?.addEventListener('click', async () => {
  const email    = (document.getElementById('login-email')    as HTMLInputElement).value.trim()
  const password = (document.getElementById('login-password') as HTMLInputElement).value
  errorEl.classList.remove('show')
  if (!email || !password) { errorEl.textContent = 'Completa todos los campos'; errorEl.classList.add('show'); return }
  if (!EMAIL_REGEX.test(email)) { errorEl.textContent = 'El correo debe tener formato usuario@dominio.com'; errorEl.classList.add('show'); return }
  try {
    const res = await authApi.login(email, password)
    localStorage.setItem('token', res.token)
    localStorage.setItem('role', res.role)
    window.location.href = res.role === 'ADMIN' ? '/admin.html' : '/index.html'
  } catch {
    errorEl.textContent = 'Email o contrasena incorrectos'
    errorEl.classList.add('show')
  }
})

document.getElementById('btn-register')?.addEventListener('click', async () => {
  const username = (document.getElementById('reg-username') as HTMLInputElement).value.trim()
  const email    = (document.getElementById('reg-email')    as HTMLInputElement).value.trim()
  const password = (document.getElementById('reg-password') as HTMLInputElement).value
  errorElReg.classList.remove('show')
  if (!username || !email || !password) { errorElReg.textContent = 'Completa todos los campos'; errorElReg.classList.add('show'); return }
  if (!EMAIL_REGEX.test(email)) { errorElReg.textContent = 'El correo debe tener formato usuario@dominio.com'; errorElReg.classList.add('show'); return }
  try {
    const res = await authApi.register(username, email, password)
    localStorage.setItem('token', res.token)
    localStorage.setItem('role', res.role)
    localStorage.setItem('username', username)
    window.location.href = '/index.html'
  } catch {
    errorElReg.textContent = 'No se pudo crear la cuenta. El correo puede ya estar registrado.'
    errorElReg.classList.add('show')
  }
})
