import type { Product, CartItem, Order, Review, SearchResult, WishlistItem, AuthResponse } from './types.js'

const BASE = {
  auth:          'http://localhost:9000',
  users:         'http://localhost:8082',
  products:      'http://localhost:8085',
  cart:          'http://localhost:8087',
  orders:        'http://localhost:8084',
  reviews:       'http://localhost:8090',
  search:        'http://localhost:8091',
  wishlist:      'http://localhost:8092',
}

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
    post<AuthResponse>(`${BASE.auth}/api/auth/login`, { email, password }),
  register: (username: string, email: string, password: string) =>
    post<AuthResponse>(`${BASE.auth}/api/auth/register`, { username, email, password }),
}

export const productsApi = {
  getAll:  () => get<Product[]>(`${BASE.products}/api/products`),
  getById: (id: number) => get<Product>(`${BASE.products}/api/products/${id}`),
}

export const cartApi = {
  getCart:    (userId: number) => get<CartItem[]>(`${BASE.cart}/api/cart/${userId}`),
  addItem:    (item: Omit<CartItem, 'id'>) => post<CartItem>(`${BASE.cart}/api/cart`, item),
  removeItem: (id: number) => del(`${BASE.cart}/api/cart/${id}`),
  clearCart:  (userId: number) => del(`${BASE.cart}/api/cart/clear/${userId}`),
}

export const ordersApi = {
  create: (order: Order) => post<Order>(`${BASE.orders}/api/orders`, order),
}

export const reviewsApi = {
  getByProduct: (productId: number) => get<Review[]>(`${BASE.reviews}/api/reviews/product/${productId}`),
  create:       (review: Review) => post<Review>(`${BASE.reviews}/api/reviews`, review),
}

export const searchApi = {
  search: (keyword: string) =>
    get<SearchResult[]>(`${BASE.search}/api/search?keyword=${encodeURIComponent(keyword)}`),
}

export const wishlistApi = {
  get:    (userId: number) => get<WishlistItem[]>(`${BASE.wishlist}/api/wishlist/${userId}`),
  add:    (item: WishlistItem) => post<WishlistItem>(`${BASE.wishlist}/api/wishlist`, item),
  remove: (id: number) => del(`${BASE.wishlist}/api/wishlist/${id}`),
}
