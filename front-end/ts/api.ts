import type { Product, CartItem, Order, Review, SearchResult, WishlistItem, AuthResponse } from './types.js'

const BASE = ''

function headers(): HeadersInit {
  const token = localStorage.getItem('token')
  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {})
  }
}

async function get<T>(url: string): Promise<T> {
  const res = await fetch(url, { headers: headers() })
  if (!res.ok) throw new Error(`${res.status}`)
  return res.json() as Promise<T>
}

async function post<T>(url: string, body: unknown): Promise<T> {
  const res = await fetch(url, { method: 'POST', headers: headers(), body: JSON.stringify(body) })
  if (!res.ok) throw new Error(`${res.status}`)
  return res.json() as Promise<T>
}

async function del(url: string): Promise<void> {
  await fetch(url, { method: 'DELETE', headers: headers() })
}

export const authApi = {
  login:    (email: string, password: string) =>
    post<AuthResponse>(`${BASE}/api/auth/login`, { email, password }),
  register: (username: string, email: string, password: string) =>
    post<AuthResponse>(`${BASE}/api/auth/register`, { username, email, password }),
}

export const productsApi = {
  getAll:  ()           => get<Product[]>(`${BASE}/api/products`),
  getById: (id: number) => get<Product>(`${BASE}/api/products/${id}`),
}

export const cartApi = {
  getCart:    (userId: number) => get<CartItem[]>(`${BASE}/api/cart/${userId}`),
  addItem:    (item: Omit<CartItem, 'id'>) => post<CartItem>(`${BASE}/api/cart`, item),
  removeItem: (id: number) => del(`${BASE}/api/cart/${id}`),
  clearCart:  (userId: number) => del(`${BASE}/api/cart/clear/${userId}`),
}

export const ordersApi = {
  create: (order: Order) => post<Order>(`${BASE}/api/orders`, order),
}

export const reviewsApi = {
  getByProduct: (productId: number) => get<Review[]>(`${BASE}/api/reviews/product/${productId}`),
  create:       (review: Review) => post<Review>(`${BASE}/api/reviews`, review),
}

export const searchApi = {
  search: (keyword: string) =>
    get<SearchResult[]>(`${BASE}/api/search?keyword=${encodeURIComponent(keyword)}`),
}

export const wishlistApi = {
  get:    (userId: number) => get<WishlistItem[]>(`${BASE}/api/wishlist/${userId}`),
  add:    (item: WishlistItem) => post<WishlistItem>(`${BASE}/api/wishlist`, item),
  remove: (id: number) => del(`${BASE}/api/wishlist/${id}`),
}
