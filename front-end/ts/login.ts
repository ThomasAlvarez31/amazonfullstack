import { authApi } from './api.js'

const errorEl  = document.getElementById('error-msg')!
const tabLogin = document.getElementById('tab-login')!
const tabReg   = document.getElementById('tab-register')!
const secLogin = document.getElementById('section-login')!
const secReg   = document.getElementById('section-register')!

function switchTab(tab: 'login' | 'register'): void {
  const isLogin = tab === 'login'
  tabLogin.style.fontWeight = isLogin ? '700' : '400'
  tabReg.style.fontWeight   = isLogin ? '400' : '700'
  secLogin.style.display    = isLogin ? 'block' : 'none'
  secReg.style.display      = isLogin ? 'none'  : 'block'
  errorEl.classList.remove('show')
}

tabLogin.addEventListener('click', () => switchTab('login'))
tabReg.addEventListener('click',   () => switchTab('register'))

function showError(msg: string): void {
  errorEl.textContent = msg
  errorEl.classList.add('show')
}

document.getElementById('btn-login')?.addEventListener('click', async () => {
  const email    = (document.getElementById('login-email') as HTMLInputElement).value.trim()
  const password = (document.getElementById('login-password') as HTMLInputElement).value
  if (!email || !password) return showError('Completa todos los campos')
  try {
    const res = await authApi.login(email, password)
    localStorage.setItem('token', res.token)
    window.location.href = '/index.html'
  } catch {
    showError('Email o contraseña incorrectos')
  }
})

document.getElementById('btn-register')?.addEventListener('click', async () => {
  const username = (document.getElementById('reg-username') as HTMLInputElement).value.trim()
  const email    = (document.getElementById('reg-email') as HTMLInputElement).value.trim()
  const password = (document.getElementById('reg-password') as HTMLInputElement).value
  if (!username || !email || !password) return showError('Completa todos los campos')
  try {
    const res = await authApi.register(username, email, password)
    localStorage.setItem('token', res.token)
    localStorage.setItem('username', username)
    window.location.href = '/index.html'
  } catch {
    showError('No se pudo crear la cuenta')
  }
})
