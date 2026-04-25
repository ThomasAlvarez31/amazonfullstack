import { renderNavbar, renderFooter, showToast, stars } from './utils.js'
import { productsApi, searchApi, cartApi, wishlistApi } from './api.js'
import type { Product } from './types.js'

renderNavbar(document.getElementById('navbar')!)
renderFooter(document.getElementById('footer')!)

const userId  = Number(localStorage.getItem('userId') ?? 1)
const grid    = document.getElementById('products-grid')!
const params  = new URLSearchParams(window.location.search)
const keyword = params.get('q') ?? ''

const titleEl = document.getElementById('page-title')
if (titleEl && keyword) titleEl.textContent = `Resultados para: "${keyword}"`

function productCard(p: Product): string {
  const rating  = Math.floor(Math.random() * 2) + 3
  const reviews = Math.floor(Math.random() * 5000) + 10
  const letter  = p.name.charAt(0).toUpperCase()
  return `
    <div class="product-card">
      <div class="product-card__img">${letter}</div>
      <div class="product-card__name">${p.name}</div>
      <div class="product-card__rating">
        <span class="stars">${stars(rating)}</span>
        <span style="color:#555">(${reviews})</span>
      </div>
      <div class="product-card__price"><sup>$</sup>${p.price.toLocaleString('es-CL')}</div>
      <div class="product-card__prime">Prime · Envio gratis</div>
      <div style="display:flex;flex-direction:column;gap:6px;margin-top:6px" onclick="event.stopPropagation()">
        <button class="btn-orange btn-orange--full" data-id="${p.id}" data-price="${p.price}" data-action="cart">Agregar al carrito</button>
        <button class="btn-ghost btn-ghost--full" data-id="${p.id}" data-action="wish">Lista de deseos</button>
      </div>
    </div>
  `
}

function attachEvents(): void {
  document.querySelectorAll<HTMLButtonElement>('[data-action="cart"]').forEach(btn => {
    btn.addEventListener('click', async () => {
      await cartApi.addItem({ userId, productId: Number(btn.dataset['id']), quantity: 1, unitPrice: Number(btn.dataset['price']) })
      showToast('Agregado al carrito')
    })
  })
  document.querySelectorAll<HTMLButtonElement>('[data-action="wish"]').forEach(btn => {
    btn.addEventListener('click', async () => {
      await wishlistApi.add({ userId, productId: Number(btn.dataset['id']) })
      showToast('Guardado en lista de deseos')
    })
  })
}

async function load(): Promise<void> {
  grid.innerHTML = '<p style="color:#555;grid-column:1/-1">Cargando...</p>'
  try {
    let products: Product[]
    if (keyword) {
      try {
        const results = await searchApi.search(keyword)
        products = results.map(r => ({ id: r.productId, name: r.name, price: r.price }))
      } catch {
        const all = await productsApi.getAll()
        products = all.filter(p => p.name.toLowerCase().includes(keyword.toLowerCase()))
      }
    } else {
      products = await productsApi.getAll()
    }

    const countEl = document.getElementById('result-count')
    if (countEl) countEl.textContent = `${products.length} resultado${products.length !== 1 ? 's' : ''}`

    grid.innerHTML = products.length
      ? products.map(productCard).join('')
      : '<p style="color:#555;grid-column:1/-1">Sin resultados.</p>'
    attachEvents()
  } catch {
    grid.innerHTML = '<p style="color:#555;grid-column:1/-1">No se pudo conectar al servidor.</p>'
  }
}

document.getElementById('price-range')?.addEventListener('input', (e) => {
  const val = (e.target as HTMLInputElement).value
  const label = document.getElementById('price-label')
  if (label) label.textContent = `Hasta $${Number(val).toLocaleString('es-CL')}`
})

load()
