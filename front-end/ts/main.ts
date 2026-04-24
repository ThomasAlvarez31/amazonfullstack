import { renderNavbar, renderFooter, showToast, stars } from './utils.js'
import { productsApi, cartApi, wishlistApi } from './api.js'
import type { Product } from './types.js'

renderNavbar(document.getElementById('navbar')!)
renderFooter(document.getElementById('footer')!)

const userId = Number(localStorage.getItem('userId') ?? 1)

const slidesEl = document.querySelector<HTMLElement>('.hero__slides')!
const dots      = document.querySelectorAll<HTMLElement>('.hero__dot')
let current     = 0

function goTo(n: number): void {
  current = (n + 3) % 3
  slidesEl.style.transform = `translateX(-${current * 100}%)`
  dots.forEach((d, i) => d.classList.toggle('active', i === current))
}

document.querySelector('.hero__btn--prev')?.addEventListener('click', () => goTo(current - 1))
document.querySelector('.hero__btn--next')?.addEventListener('click', () => goTo(current + 1))
dots.forEach((d, i) => d.addEventListener('click', () => goTo(i)))
setInterval(() => goTo(current + 1), 5000)

const categories = [
  { name: 'Electronica', letter: 'E', color: '#007185', q: 'laptop'   },
  { name: 'Ropa',        letter: 'R', color: '#c45500', q: 'ropa'     },
  { name: 'Hogar',       letter: 'H', color: '#5a3e28', q: 'hogar'    },
  { name: 'Libros',      letter: 'L', color: '#232f3e', q: 'libro'    },
  { name: 'Juguetes',    letter: 'J', color: '#d13212', q: 'juguete'  },
  { name: 'Deportes',    letter: 'D', color: '#1e7e34', q: 'deporte'  },
  { name: 'Belleza',     letter: 'B', color: '#8e24aa', q: 'belleza'  },
  { name: 'Alimentos',   letter: 'A', color: '#ff9900', q: 'alimento' },
]

const catGrid = document.getElementById('category-grid')!
catGrid.innerHTML = categories.map(c => `
  <div class="category-card" onclick="location.href='/products.html?q=${c.q}'">
    <h3>${c.name}</h3>
    <div class="category-card__img" style="background:${c.color}">${c.letter}</div>
    <a href="/products.html?q=${c.q}">Ver mas</a>
  </div>
`).join('')

function productCard(p: Product): string {
  const rating  = Math.floor(Math.random() * 2) + 3
  const reviews = Math.floor(Math.random() * 3000) + 50
  const letter  = p.name.charAt(0).toUpperCase()
  return `
    <div class="product-card">
      <div class="product-card__img">${letter}</div>
      <div class="product-card__name">${p.name}</div>
      <div class="product-card__rating">
        <span class="stars">${stars(rating)}</span>
        <span style="color:#555">(${reviews})</span>
      </div>
      <div class="product-card__price"><span class="product-card__price-small">$</span>${p.price.toLocaleString('es-CL')}</div>
      <div class="product-card__prime">Prime - Entrega gratis</div>
      <div class="product-card__btn">
        <button class="btn-add-cart" data-id="${p.id}" data-price="${p.price}">Agregar al carrito</button>
        <button class="btn-wishlist" data-id="${p.id}">Lista de deseos</button>
      </div>
    </div>
  `
}

async function loadProducts(): Promise<void> {
  const grid = document.getElementById('products-grid')!
  try {
    const products = await productsApi.getAll()
    grid.innerHTML = products.length
      ? products.map(productCard).join('')
      : '<p style="color:#555;grid-column:1/-1">No hay productos aun. Agrega desde MS-Products.</p>'
    attachEvents(products)
  } catch {
    grid.innerHTML = '<p style="color:#555;grid-column:1/-1">MS-Products no disponible en este momento.</p>'
  }
}

function attachEvents(products: Product[]): void {
  document.querySelectorAll<HTMLButtonElement>('.btn-add-cart').forEach(btn => {
    btn.addEventListener('click', async (e) => {
      e.stopPropagation()
      await cartApi.addItem({ userId, productId: Number(btn.dataset['id']), quantity: 1, unitPrice: Number(btn.dataset['price']) })
      showToast('Agregado al carrito')
      const c = document.getElementById('cart-count')
      if (c) c.textContent = String(Number(c.textContent) + 1)
    })
  })
  document.querySelectorAll<HTMLButtonElement>('.btn-wishlist').forEach(btn => {
    btn.addEventListener('click', async (e) => {
      e.stopPropagation()
      await wishlistApi.add({ userId, productId: Number(btn.dataset['id']) })
      showToast('Guardado en lista de deseos')
    })
  })
}

loadProducts()
