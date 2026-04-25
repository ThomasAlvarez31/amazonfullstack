import { renderNavbar, renderFooter, showToast, stars } from './utils.js';
import { productsApi, cartApi, wishlistApi } from './api.js';
renderNavbar(document.getElementById('navbar'));
renderFooter(document.getElementById('footer'));
const userId = Number(localStorage.getItem('userId') ?? 1);
const root = document.getElementById('detail-root');
const id = Number(new URLSearchParams(location.search).get('id'));
function fmt(n) {
    return '$' + n.toLocaleString('es-CL');
}
function productCard(p) {
    return `
    <div class="product-card" onclick="location.href='/detail.html?id=${p.id}'" style="cursor:pointer">
      <div class="product-card__img">${p.name.charAt(0)}</div>
      <div class="product-card__name">${p.name}</div>
      <div class="product-card__price"><sup>$</sup>${p.price.toLocaleString('es-CL')}</div>
      <div class="product-card__prime">Prime - Entrega gratis</div>
    </div>`;
}
async function load() {
    if (!id) {
        root.innerHTML = '<p style="color:#b12704;padding:20px 0">Producto no encontrado. <a href="/products.html" style="color:#007185">Volver</a></p>';
        return;
    }
    try {
        const p = await productsApi.getById(id);
        const allProducts = await productsApi.getAll();
        const related = allProducts.filter(x => x.id !== p.id).slice(0, 4);
        document.title = p.name + ' — Amazon.cl';
        root.innerHTML = `
      <div class="breadcrumb">
        <a href="/index.html">Amazon.cl</a> ›
        <a href="/products.html">Productos</a> ›
        <span style="color:#0f1111">${p.name.slice(0, 50)}${p.name.length > 50 ? '...' : ''}</span>
      </div>

      <div class="detail-layout">
        <div class="detail-img">${p.name.charAt(0)}</div>

        <div class="detail-info">
          <h1 class="detail-title">${p.name}</h1>
          <div style="display:flex;align-items:center;gap:8px;margin-bottom:10px">
            <span class="stars">${stars(4)}</span>
            <a href="#" style="font-size:13px;color:#007185">Ver valoraciones</a>
          </div>
          <div style="border-top:1px solid #eee;border-bottom:1px solid #eee;padding:12px 0;margin:10px 0">
            <div class="detail-price"><sup>$</sup>${p.price.toLocaleString('es-CL')}</div>
            <div style="font-size:12px;color:#00a8e0;margin-top:4px">Prime - Entrega gratis</div>
          </div>
          ${p.description ? `<div style="margin-bottom:14px"><div style="font-size:12px;font-weight:700;color:#555;margin-bottom:6px">DESCRIPCION</div><p style="font-size:13px;line-height:1.6">${p.description}</p></div>` : ''}
          <div class="detail-stock-ok" style="margin-bottom:14px">En stock</div>
          <div style="display:flex;align-items:center;gap:10px;margin-bottom:12px">
            <label style="font-size:12px;font-weight:700">Cantidad:</label>
            <select id="qty-select" style="padding:4px 8px;border:1px solid #adb1b8;border-radius:3px;font-size:13px">
              ${[1, 2, 3, 4, 5].map(n => `<option value="${n}">${n}</option>`).join('')}
            </select>
          </div>
          <div style="display:flex;gap:8px;flex-wrap:wrap">
            <button class="btn-orange btn-orange--rect" id="btn-add-cart">Agregar al carrito</button>
            <button class="btn-ghost" style="border-radius:4px;padding:8px 18px;font-size:13px" id="btn-wishlist">Lista de deseos</button>
          </div>
        </div>

        <div class="buy-box">
          <div style="font-size:22px;font-weight:700;margin-bottom:6px">${fmt(p.price)}</div>
          <div style="font-size:11px;color:#00a8e0;margin-bottom:10px">Prime - Entrega gratis</div>
          <div class="detail-stock-ok" style="margin-bottom:12px">En stock</div>
          <button class="btn-orange btn-orange--full" style="margin-bottom:8px;padding:9px 0;font-size:13px;border-radius:4px" id="btn-buy-now">
            Comprar ahora
          </button>
          <button class="btn-orange btn-orange--full" style="padding:9px 0;font-size:13px;border-radius:4px" id="btn-add-cart-box">
            Agregar al carrito
          </button>
          <p style="font-size:10px;color:#555;text-align:center;margin-top:8px">Pago seguro</p>
        </div>
      </div>

      ${related.length ? `
      <h2 class="section-title" style="margin-top:24px">Tambien te puede interesar</h2>
      <div class="products-grid">${related.map(productCard).join('')}</div>` : ''}
    `;
        const qty = () => Number(document.getElementById('qty-select').value);
        document.getElementById('btn-add-cart')?.addEventListener('click', async () => {
            await cartApi.addItem({ userId, productId: p.id, quantity: qty(), unitPrice: p.price });
            showToast('Agregado al carrito');
        });
        document.getElementById('btn-add-cart-box')?.addEventListener('click', async () => {
            await cartApi.addItem({ userId, productId: p.id, quantity: 1, unitPrice: p.price });
            showToast('Agregado al carrito');
        });
        document.getElementById('btn-buy-now')?.addEventListener('click', async () => {
            await cartApi.addItem({ userId, productId: p.id, quantity: 1, unitPrice: p.price });
            location.href = '/cart.html';
        });
        document.getElementById('btn-wishlist')?.addEventListener('click', async () => {
            await wishlistApi.add({ userId, productId: p.id });
            showToast('Guardado en lista de deseos');
        });
    }
    catch {
        root.innerHTML = '<p style="color:#b12704;padding:20px 0">No se pudo cargar el producto. <a href="/products.html" style="color:#007185">Volver</a></p>';
    }
}
load();
