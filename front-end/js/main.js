import { renderNavbar, renderFooter, showToast, stars } from './utils.js';
import { productsApi, cartApi, wishlistApi } from './api.js';
renderNavbar(document.getElementById('navbar'));
renderFooter(document.getElementById('footer'));
const userId = Number(localStorage.getItem('userId') ?? 1);
// Hero carousel
const slidesEl = document.querySelector('.hero__slides');
const dots = document.querySelectorAll('.hero__dot');
let current = 0;
function goTo(n) {
    current = (n + 3) % 3;
    slidesEl.style.transform = `translateX(-${current * 100}%)`;
    dots.forEach((d, i) => d.classList.toggle('active', i === current));
}
document.querySelector('.hero__btn--prev')?.addEventListener('click', () => goTo(current - 1));
document.querySelector('.hero__btn--next')?.addEventListener('click', () => goTo(current + 1));
dots.forEach((d, i) => d.addEventListener('click', () => goTo(i)));
setInterval(() => goTo(current + 1), 5000);
// Categories
const categories = [
    { name: 'Electrónica', icon: '💻', q: 'laptop' },
    { name: 'Ropa', icon: '👕', q: 'ropa' },
    { name: 'Hogar', icon: '🏠', q: 'hogar' },
    { name: 'Libros', icon: '📚', q: 'libro' },
    { name: 'Juguetes', icon: '🧸', q: 'juguete' },
    { name: 'Deportes', icon: '⚽', q: 'deporte' },
    { name: 'Belleza', icon: '💄', q: 'belleza' },
    { name: 'Alimentos', icon: '🛒', q: 'alimento' },
];
const catGrid = document.getElementById('category-grid');
catGrid.innerHTML = categories.map(c => `
  <div class="category-card" onclick="location.href='/products.html?q=${c.q}'">
    <h3>${c.name}</h3>
    <div class="category-card__img">${c.icon}</div>
    <a href="/products.html?q=${c.q}">Ver más</a>
  </div>
`).join('');
function productCard(p) {
    const rating = Math.floor(Math.random() * 2) + 3;
    const reviews = Math.floor(Math.random() * 3000) + 50;
    return `
    <div class="product-card">
      <div class="product-card__img">📦</div>
      <div class="product-card__name">${p.name}</div>
      <div class="product-card__rating">
        <span class="stars">${stars(rating)}</span>
        <span style="color:#555">(${reviews})</span>
      </div>
      <div class="product-card__price"><span class="product-card__price-small">$</span>${p.price.toLocaleString('es-CL')}</div>
      <div class="product-card__prime">✓ Prime — Entrega gratis</div>
      <div class="product-card__btn">
        <button class="btn-add-cart" data-id="${p.id}" data-price="${p.price}">Agregar al carrito</button>
        <button class="btn-wishlist" data-id="${p.id}">♡ Lista de deseos</button>
      </div>
    </div>
  `;
}
async function loadProducts() {
    const grid = document.getElementById('products-grid');
    try {
        const products = await productsApi.getAll();
        grid.innerHTML = products.length
            ? products.map(productCard).join('')
            : '<p style="color:#555;grid-column:1/-1">No hay productos aún. Agrega desde MS-Products.</p>';
        attachEvents(products);
    }
    catch {
        grid.innerHTML = '<p style="color:#555;grid-column:1/-1">MS-Products no disponible en este momento.</p>';
    }
}
function attachEvents(products) {
    document.querySelectorAll('.btn-add-cart').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.stopPropagation();
            await cartApi.addItem({ userId, productId: Number(btn.dataset['id']), quantity: 1, unitPrice: Number(btn.dataset['price']) });
            showToast('✓ Agregado al carrito');
            const c = document.getElementById('cart-count');
            if (c)
                c.textContent = String(Number(c.textContent) + 1);
        });
    });
    document.querySelectorAll('.btn-wishlist').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.stopPropagation();
            await wishlistApi.add({ userId, productId: Number(btn.dataset['id']) });
            showToast('♡ Guardado en lista de deseos');
        });
    });
}
loadProducts();
