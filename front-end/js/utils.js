import { cartApi } from './api.js';
export function renderNavbar(el) {
    const username = localStorage.getItem('username') ?? 'Identificate';
    const userId = Number(localStorage.getItem('userId') ?? 1);
    const loggedIn = !!localStorage.getItem('token');
    const isAdmin = localStorage.getItem('role') === 'ADMIN';
    el.innerHTML = `
    <nav class="navbar">
      <button class="navbar__logo" onclick="location.href='/index.html'" style="background:none;border:none;padding:4px;line-height:0">
        <img src="/assets/logo.svg" height="36" alt="Amazon.cl" style="display:block" />
      </button>
      <div class="navbar__location">
        <span>Entregar a</span>
        <span>Chile</span>
      </div>
      <div class="navbar__search">
        <select>
          <option>Todo</option>
          <option>Electronica</option>
          <option>Ropa</option>
          <option>Libros</option>
          <option>Hogar</option>
        </select>
        <input id="nav-search" type="text" placeholder="Buscar en Amazon.cl" />
        <button id="nav-search-btn">&#9906;</button>
      </div>
      <div class="navbar__item" id="nav-account" style="cursor:pointer">
        <span>Hola, ${loggedIn ? username : 'Identificate'}</span>
        <span>${loggedIn ? 'Cerrar sesion' : 'Cuenta y listas'}</span>
      </div>
      <button class="navbar__iconbtn" onclick="location.href='/wishlist.html'">
        Lista de deseos
      </button>
      <button class="navbar__iconbtn" onclick="location.href='/cart.html'">
        Carrito&nbsp;<span class="navbar__badge" id="cart-count">0</span>
      </button>
      ${isAdmin ? '<button class="navbar__admin" onclick="location.href=\'/admin.html\'">Admin</button>' : ''}
    </nav>
    <nav class="subnav">
      <button class="bold" onclick="location.href='/products.html'">&#9776; Todas las categorias</button>
      <button onclick="location.href='/products.html'">Ofertas del dia</button>
      <button onclick="location.href='/products.html'">Productos</button>
      <button onclick="location.href='/products.html?q=electronica'">Electronica</button>
      <button onclick="location.href='/products.html?q=ropa'">Ropa</button>
      <button onclick="location.href='/products.html?q=hogar'">Hogar</button>
      <button onclick="location.href='/products.html?q=libros'">Libros</button>
      <button onclick="location.href='/products.html?q=juguetes'">Juguetes</button>
    </nav>
  `;
    document.getElementById('nav-search-btn')?.addEventListener('click', () => {
        const q = document.getElementById('nav-search').value.trim();
        if (q)
            window.location.href = `/products.html?q=${encodeURIComponent(q)}`;
    });
    document.getElementById('nav-search')?.addEventListener('keydown', (e) => {
        if (e.key === 'Enter')
            document.getElementById('nav-search-btn')?.click();
    });
    document.getElementById('nav-account')?.addEventListener('click', () => {
        if (localStorage.getItem('token')) {
            localStorage.clear();
            window.location.href = '/index.html';
        }
        else {
            window.location.href = '/login.html';
        }
    });
    cartApi.getCart(userId).then(items => {
        const n = items.reduce((s, i) => s + i.quantity, 0);
        const badge = document.getElementById('cart-count');
        if (badge)
            badge.textContent = String(n);
    }).catch(() => { });
}
export function renderFooter(el) {
    el.innerHTML = `
    <div class="footer-back" onclick="window.scrollTo({top:0,behavior:'smooth'})">Volver al principio &#8593;</div>
    <div class="footer-main">
      <div class="footer-grid">
        <div class="footer-col">
          <h4>Conocenos</h4>
          <a href="#">Carreras</a>
          <a href="#">Blog</a>
          <a href="#">Acerca de</a>
        </div>
        <div class="footer-col">
          <h4>Gana dinero</h4>
          <a href="#">Vende en Amazon</a>
          <a href="#">Afiliados</a>
        </div>
        <div class="footer-col">
          <h4>Pagos</h4>
          <a href="#">Tarjeta de credito</a>
          <a href="#">WebPay</a>
        </div>
        <div class="footer-col">
          <h4>Ayuda</h4>
          <a href="#">Rastrear pedido</a>
          <a href="#">Devoluciones</a>
        </div>
      </div>
    </div>
    <div class="footer-bottom">
      <img src="/assets/logo-dark.svg" height="28" alt="Amazon.cl" style="filter:brightness(0) invert(1);margin-bottom:6px" />
      <p style="font-size:11px;color:#777">2026 - Recreacion academica</p>
    </div>
  `;
}
export function showToast(msg) {
    let toast = document.getElementById('toast');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast';
        toast.className = 'toast';
        toast.innerHTML = '<span class="toast-icon">&#10003;</span><span id="toast-msg"></span>';
        document.body.appendChild(toast);
    }
    const msgEl = document.getElementById('toast-msg');
    if (msgEl)
        msgEl.textContent = msg;
    toast.classList.add('show');
    setTimeout(() => toast.classList.remove('show'), 2800);
}
export function stars(n) {
    return '&#9733;'.repeat(Math.round(n)) + '&#9734;'.repeat(5 - Math.round(n));
}
