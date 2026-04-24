import { renderNavbar, renderFooter, showToast } from './utils.js'
import { cartApi, ordersApi } from './api.js'

renderNavbar(document.getElementById('navbar')!)
renderFooter(document.getElementById('footer')!)

const userId     = Number(localStorage.getItem('userId') ?? 1)
const itemsEl    = document.getElementById('cart-items')!
const subtotalEl = document.getElementById('cart-subtotal')!
const countEl    = document.getElementById('cart-item-count')!

async function loadCart(): Promise<void> {
  try {
    const items = await cartApi.getCart(userId)

    if (items.length === 0) {
      itemsEl.innerHTML = `
        <div class="cart-empty">
          <h2>Tu carrito esta vacio</h2>
          <p>Explora miles de productos y agrega los que mas te gusten.</p>
          <a href="/index.html">Ir a la tienda</a>
        </div>`
      subtotalEl.textContent = '$0'
      countEl.textContent = '0 articulos'
      return
    }

    const total = items.reduce((s, i) => s + i.unitPrice * i.quantity, 0)
    const count = items.reduce((s, i) => s + i.quantity, 0)
    subtotalEl.textContent = `$${total.toLocaleString('es-CL')}`
    countEl.textContent = `${count} articulo${count !== 1 ? 's' : ''}`

    itemsEl.innerHTML = `
      <h1>Carrito de compras</h1>
      ${items.map(item => `
        <div class="cart-item">
          <div class="cart-item__img">${item.productId}</div>
          <div class="cart-item__info">
            <div class="cart-item__name">Producto #${item.productId}</div>
            <div class="cart-item__price">$${(item.unitPrice * item.quantity).toLocaleString('es-CL')}</div>
            <div style="font-size:12px;color:green;margin-top:4px">En stock</div>
            <div class="cart-item__qty">
              <span>Cantidad: ${item.quantity}</span>
            </div>
            <button class="cart-item__remove" data-id="${item.id}">Eliminar</button>
          </div>
        </div>
      `).join('')}
    `

    document.querySelectorAll<HTMLButtonElement>('.cart-item__remove').forEach(btn => {
      btn.addEventListener('click', async () => {
        await cartApi.removeItem(Number(btn.dataset['id']))
        showToast('Articulo eliminado')
        loadCart()
      })
    })
  } catch {
    itemsEl.innerHTML = '<p style="color:#555">No se pudo cargar el carrito.</p>'
  }
}

document.getElementById('btn-checkout')?.addEventListener('click', async () => {
  try {
    const items = await cartApi.getCart(userId)
    if (items.length === 0) return showToast('El carrito esta vacio')
    for (const item of items) {
      await ordersApi.create({ userId, productId: item.productId, quantity: item.quantity, status: 'PENDIENTE', totalPrice: item.unitPrice * item.quantity })
    }
    await cartApi.clearCart(userId)
    showToast('Pedido realizado con exito')
    setTimeout(loadCart, 1500)
  } catch {
    showToast('Error al procesar el pedido')
  }
})

loadCart()
