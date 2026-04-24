export interface Product {
  id: number
  name: string
  price: number
  description?: string
  category?: string
}

export interface CartItem {
  id: number
  userId: number
  productId: number
  quantity: number
  unitPrice: number
}

export interface Order {
  id?: number
  userId: number
  productId: number
  quantity: number
  status: string
  totalPrice: number
}

export interface Review {
  id?: number
  productId: number
  userId: number
  rating: number
  comment: string
}

export interface SearchResult {
  id: number
  productId: number
  name: string
  category: string
  price: number
}

export interface WishlistItem {
  id?: number
  userId: number
  productId: number
}

export interface AuthResponse {
  token: string
  role: string
}
