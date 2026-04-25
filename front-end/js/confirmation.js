import { renderNavbar, renderFooter } from './utils.js';
renderNavbar(document.getElementById('navbar'));
renderFooter(document.getElementById('footer'));
const params = new URLSearchParams(location.search);
const total = Number(params.get('total') ?? 0);
const orderNum = 'AZ-' + Math.floor(Math.random() * 900000 + 100000);
const info = document.getElementById('order-info');
info.innerHTML = `N de pedido: <strong>${orderNum}</strong>${total ? ` &middot; Total: <strong>$${total.toLocaleString('es-CL')}</strong>` : ''}`;
document.title = `Pedido ${orderNum} confirmado — Amazon.cl`;
