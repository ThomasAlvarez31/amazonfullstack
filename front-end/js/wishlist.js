import { renderNavbar, renderFooter, showToast } from './utils.js';
import { wishlistApi, cartApi, productsApi } from './api.js';
renderNavbar(document.getElementById('navbar'));
renderFooter(document.getElementById('footer'));
const userId = Number(localStorage.getItem('userId') ?? 1);
const body = document.getElementById('wishlist-body');
async function load() {
    try {
        const items = await wishlistApi.get(userId);
        const products = await productsApi.getAll();
        const map = new Map(products.map(p => [p.id, p]));
        if (items.length === 0) {
            body.innerHTML = `
        <div style="background:#fff;border-radius:4px;padding:40px;text-align:center;box-shadow:0 1px 3px rgba(0,0,0,.08)">
          <div style="font-size:48px;margin-bottom:12px">&#9825;</div>
          <h2 style="font-size:18px;margin-bottom:8px">Tu lista de deseos esta vacia</h2>
          <p style="color:#555;margin-bottom:18px;font-size:13px">Guarda productos que te interesen para comprarlos despues</p>
          <a href="/products.html" class="btn-orange btn-orange--rect">Explorar productos</a>
        </div>`;
            return;
        }
        body.innerHTML = items.map(item => {
            const p = map.get(item.productId);
            const name = p ? p.name : `Producto #${item.productId}`;
            const price = p ? p.price : 0;
            return `
        <div class="wishlist-item" data-wish-id="${item.id}" data-product-id="${item.productId}">
          <div style="background:#f0f2f2;border-radius:4px;min-width:80px;height:80px;display:flex;align-items:center;justify-content:center;font-size:24px;font-weight:800;color:#bbb;flex-shrink:0">
            ${name.charAt(0)}
          </div>
          <div style="flex:1">
            <a href="/detail.html?id=${item.productId}" style="font-size:15px;color:#0f1111;display:block;margin-bottom:4px">${name}</a>
            <div style="font-size:17px;font-weight:700;margin-bottom:4px">$${price.toLocaleString('es-CL')}</div>
            <div style="font-size:11px;color:#00a8e0">Prime - Entrega gratis</div>
          </div>
          <div style="display:flex;flex-direction:column;gap:8px;align-items:flex-end">
            <button class="btn-orange" style="padding:7px 18px;border-radius:4px;font-size:12px" data-action="cart" data-wish-id="${item.id}" data-product-id="${item.productId}" data-price="${price}">
              Agregar al carrito
            </button>
            <button data-action="remove" data-wish-id="${item.id}" style="background:none;border:none;color:#007185;font-size:11px;cursor:pointer;text-decoration:underline;font-family:inherit">
              Eliminar
            </button>
          </div>
        </div>`;
        }).join('');
        body.querySelectorAll('[data-action="cart"]').forEach(btn => {
            btn.addEventListener('click', async () => {
                const productId = Number(btn.dataset['productId']);
                const price = Number(btn.dataset['price']);
                const wishId = Number(btn.dataset['wishId']);
                await cartApi.addItem({ userId, productId, quantity: 1, unitPrice: price });
                await wishlistApi.remove(wishId);
                showToast('Movido al carrito');
                load();
            });
        });
        body.querySelectorAll('[data-action="remove"]').forEach(btn => {
            btn.addEventListener('click', async () => {
                await wishlistApi.remove(Number(btn.dataset['wishId']));
                showToast('Eliminado de la lista');
                load();
            });
        });
    }
    catch {
        body.innerHTML = '<p style="color:#555">No se pudo cargar la lista de deseos.</p>';
    }
}
load();
