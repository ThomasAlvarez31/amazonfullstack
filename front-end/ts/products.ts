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
  return `
    <div class="product-card">
      <div class="product-card__img">📦</div>
      <div class="product-card__name">${p.name}</div>
      <div class="product-card__rating">
        <span class="stars">${stars(rating)}</span>
        <span style="color:#555">(${reviews})</span>
      </div>
      <div class="product-card__price"><span class="product-card__price-small">$</span>${p.price.toLocaleString('es-CL')}</div>
      <div class="product-card__prime">✓ Prime — Envío gratis</div>
      <div class="product-card__btn">
        <button class="btn-add-cart" data-id="${p.id}" data-price="${p.price}">Agregar al carrito</button>
        <button class="btn-wishlist" data-id="${p.id}">♡ Lista de deseos</button>
      </div>
    </div>
  `
}

function attachEvents(): void {
  document.querySelectorAll<HTMLButtonElement>('.btn-add-cart').forEach(btn => {
    btn.addEventListener('click', async (e) => {
      e.stopPropagation()
      await cartApi.addItem({ userId, productId: Number(btn.dataset['id']), quantity: 1, unitPrice: Number(btn.dataset['price']) })
      showToast('✓ Agregado al carrito')
    })
  })
  document.querySelectorAll<HTMLButtonElement>('.btn-wishlist').forEach(btn => {
    btn.addEventListener('click', async (e) => {
      e.stopPropagation()
      await wishlistApi.add({ userId, productId: Number(btn.dataset['id']) })
      showToast('♡ Guardado en lista de deseos')
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
