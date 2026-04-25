import { renderNavbar, renderFooter, showToast } from './utils.js';
import { cartApi, ordersApi } from './api.js';
renderNavbar(document.getElementById('navbar'));
renderFooter(document.getElementById('footer'));
const userId = Number(localStorage.getItem('userId') ?? 1);
const stepsEl = document.getElementById('steps');
const mainEl = document.getElementById('checkout-main');
const sideEl = document.getElementById('checkout-sidebar');
const STEPS = ['Envio', 'Pago', 'Confirmar'];
let step = 1;
let items = [];
function fmt(n) {
    return '$' + n.toLocaleString('es-CL');
}
function subtotal() { return items.reduce((s, i) => s + i.unitPrice * i.quantity, 0); }
function shipping() { return subtotal() > 50000 ? 0 : 4990; }
function total() { return subtotal() + shipping(); }
function renderSteps() {
    stepsEl.innerHTML = STEPS.map((s, i) => `
    <div class="checkout-step ${i + 1 === step ? 'active' : i + 1 < step ? 'done' : ''}">
      ${i + 1 < step ? '&#10003; ' : ''}${s}
    </div>`).join('');
}
function renderSidebar() {
    sideEl.innerHTML = `
    <div style="font-weight:700;margin-bottom:10px;border-bottom:1px solid #eee;padding-bottom:8px">Resumen del pedido</div>
    ${items.map(i => `
      <div style="display:flex;justify-content:space-between;padding:4px 0;border-bottom:1px solid #f5f5f5;font-size:12px">
        <span>Producto #${i.productId} x${i.quantity}</span>
        <span>${fmt(i.unitPrice * i.quantity)}</span>
      </div>`).join('')}
    <div style="display:flex;justify-content:space-between;margin-top:8px"><span>Subtotal</span><span>${fmt(subtotal())}</span></div>
    <div style="display:flex;justify-content:space-between;margin-top:4px">
      <span>Envio</span>
      <span style="color:${shipping() === 0 ? '#007600' : 'inherit'}">${shipping() === 0 ? 'Gratis' : fmt(shipping())}</span>
    </div>
    <div style="display:flex;justify-content:space-between;font-weight:700;font-size:15px;border-top:1px solid #eee;padding-top:8px;margin-top:8px">
      <span>Total</span><span>${fmt(total())}</span>
    </div>
    ${shipping() > 0 ? `<p style="font-size:10px;color:#555;margin-top:8px">Agrega ${fmt(50000 - subtotal())} mas para envio gratis.</p>` : ''}
  `;
}
function renderStep1() {
    mainEl.innerHTML = `
    <h2 style="font-size:18px;font-weight:700;margin-bottom:16px">Direccion de envio</h2>
    <div class="form-group"><label>Nombre completo</label><input type="text" id="s-name" placeholder="Nombre y apellido"/></div>
    <div class="form-group"><label>Direccion</label><input type="text" id="s-street" placeholder="Calle y numero"/></div>
    <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:16px">
      <div class="form-group" style="margin:0"><label>Ciudad</label><input type="text" id="s-city" value="Santiago"/></div>
      <div class="form-group" style="margin:0"><label>Region</label>
        <select style="width:100%;padding:8px 10px;border:1px solid #adb1b8;border-radius:3px;font-size:12px;outline:none">
          <option>Region Metropolitana</option><option>Valparaiso</option><option>Biobio</option><option>Araucania</option>
        </select>
      </div>
    </div>
    <button class="btn-orange btn-orange--rect" id="btn-next-1">Continuar al pago</button>
  `;
    document.getElementById('btn-next-1')?.addEventListener('click', () => { step = 2; renderAll(); });
}
function renderStep2() {
    mainEl.innerHTML = `
    <h2 style="font-size:18px;font-weight:700;margin-bottom:16px">Metodo de pago</h2>
    <div style="display:flex;gap:10px;margin-bottom:16px;flex-wrap:wrap">
      ${['Tarjeta de credito', 'WebPay Plus', 'Transferencia'].map((m, i) => `
        <label style="display:flex;align-items:center;gap:6px;padding:8px 14px;border:1px solid ${i === 0 ? '#e77600' : '#ddd'};border-radius:3px;cursor:pointer;font-size:12px;font-weight:${i === 0 ? 700 : 400}">
          <input type="radio" name="pay" ${i === 0 ? 'checked' : ''} style="accent-color:#ff9900">${m}
        </label>`).join('')}
    </div>
    <div class="form-group"><label>Numero de tarjeta</label><input type="text" placeholder="**** **** **** ****" style="max-width:280px"/></div>
    <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:16px;max-width:280px">
      <div class="form-group" style="margin:0"><label>Vencimiento</label><input type="text" placeholder="MM/AA"/></div>
      <div class="form-group" style="margin:0"><label>CVV</label><input type="text" placeholder="***"/></div>
    </div>
    <div style="display:flex;gap:10px">
      <button class="btn-ghost" style="border-radius:4px;padding:8px 18px;font-size:13px" id="btn-back-2">Volver</button>
      <button class="btn-orange btn-orange--rect" id="btn-next-2">Revisar pedido</button>
    </div>
  `;
    document.getElementById('btn-back-2')?.addEventListener('click', () => { step = 1; renderAll(); });
    document.getElementById('btn-next-2')?.addEventListener('click', () => { step = 3; renderAll(); });
}
function renderStep3() {
    mainEl.innerHTML = `
    <h2 style="font-size:18px;font-weight:700;margin-bottom:16px">Confirmar pedido</h2>
    <div style="background:#f0f2f2;border-radius:4px;padding:14px;margin-bottom:16px">
      <div style="font-size:12px;font-weight:700;color:#555;margin-bottom:8px">PRODUCTOS</div>
      ${items.map(i => `
        <div style="display:flex;justify-content:space-between;font-size:13px;padding:5px 0;border-bottom:1px solid #e5e5e5">
          <span>Producto #${i.productId} x${i.quantity}</span>
          <span style="font-weight:600">${fmt(i.unitPrice * i.quantity)}</span>
        </div>`).join('')}
      <div style="display:flex;justify-content:space-between;font-weight:700;font-size:15px;border-top:2px solid #ddd;padding-top:8px;margin-top:8px">
        <span>Total</span><span>${fmt(total())}</span>
      </div>
    </div>
    <div style="display:flex;gap:10px">
      <button class="btn-ghost" style="border-radius:4px;padding:8px 18px;font-size:13px" id="btn-back-3">Volver</button>
      <button class="btn-orange btn-orange--rect" id="btn-confirm">Confirmar y pagar</button>
    </div>
  `;
    document.getElementById('btn-back-3')?.addEventListener('click', () => { step = 2; renderAll(); });
    document.getElementById('btn-confirm')?.addEventListener('click', confirmOrder);
}
function renderAll() {
    renderSteps();
    renderSidebar();
    if (step === 1)
        renderStep1();
    else if (step === 2)
        renderStep2();
    else
        renderStep3();
}
async function confirmOrder() {
    try {
        for (const item of items) {
            await ordersApi.create({
                userId,
                productId: item.productId,
                quantity: item.quantity,
                status: 'PENDIENTE',
                totalPrice: item.unitPrice * item.quantity
            });
        }
        await cartApi.clearCart(userId);
        location.href = `/confirmation.html?total=${total()}`;
    }
    catch {
        showToast('Error al procesar el pedido');
    }
}
async function init() {
    items = await cartApi.getCart(userId);
    if (items.length === 0) {
        location.href = '/cart.html';
        return;
    }
    renderAll();
}
init();
