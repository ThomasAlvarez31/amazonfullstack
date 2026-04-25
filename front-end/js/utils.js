import { cartApi } from './api.js';
export function renderNavbar(el) {
    const username = localStorage.getItem('username') ?? 'Identificate';
    const userId = Number(localStorage.getItem('userId') ?? 1);
    el.innerHTML = `
    <nav class="navbar">
      <a class="navbar__logo" href="/index.html">
        <span>amazon<em>.cl</em></span>
      </a>
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
        <button id="nav-search-btn">Buscar</button>
      </div>
      <div class="navbar__account" id="nav-account">
        <span>Hola, ${username}</span>
        <span>${localStorage.getItem('token') ? 'Cerrar sesion' : 'Cuenta y listas'}</span>
      </div>
      <button class="navbar__iconbtn" onclick="location.href='/wishlist.html'">
        Lista de deseos
      </button>
      <button class="navbar__iconbtn" onclick="location.href='/cart.html'">
        Carrito <span class="navbar__badge" id="cart-count">0</span>
      </button>
      ${localStorage.getItem('role') === 'ADMIN' ? '<button class="navbar__admin" onclick="location.href=\'/admin.html\'">Admin</button>' : ''}
    </nav>
    <nav class="subnav">
      <a class="subnav__menu" href="#">&#9776; Todas las categorias</a>
      <a href="#">Ofertas del dia</a>
      <a href="/products.html">Productos</a>
      <a href="#">Electronica</a>
      <a href="#">Ropa</a>
      <a href="#">Hogar</a>
      <a href="#">Libros</a>
      <a href="#">Juguetes</a>
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
            window.location.reload();
        }
        else
            window.location.href = '/login.html';
    });
    cartApi.getCart(userId).then(items => {
        const n = items.reduce((s, i) => s + i.quantity, 0);
        const el = document.getElementById('cart-count');
        if (el)
            el.textContent = String(n);
    }).catch(() => { });
}
export function renderFooter(el) {
    el.innerHTML = `
    <div class="footer-back" onclick="window.scrollTo({top:0,behavior:'smooth'})">Volver al principio</div>
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
      <strong>amazon<em>.cl</em></strong>
      <p style="margin-top:8px">2025 - Recreacion academica</p>
    </div>
  `;
}
export function showToast(msg) {
    let toast = document.getElementById('toast');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast';
        toast.className = 'toast';
        document.body.appendChild(toast);
    }
    toast.textContent = msg;
    toast.classList.add('show');
    setTimeout(() => toast.classList.remove('show'), 2800);
}
export function stars(n) {
    return '&#9733;'.repeat(Math.round(n)) + '&#9734;'.repeat(5 - Math.round(n));
}
