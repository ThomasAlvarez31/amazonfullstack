import { describe, it, expect, vi, beforeEach } from 'vitest'
import { authApi, productsApi, cartApi, ordersApi, searchApi, wishlistApi } from '../api'

function mockFetch(data: unknown, ok = true, status = 200) {
  global.fetch = vi.fn().mockResolvedValue({
    ok,
    status,
    json: () => Promise.resolve(data),
  } as Response)
}

beforeEach(() => {
  localStorage.clear()
  vi.clearAllMocks()
})

describe('authApi', () => {
  it('login envía POST y retorna AuthResponse', async () => {
    mockFetch({ token: 'jwt-test', role: 'USER' })
    const res = await authApi.login('user@test.com', 'pass123')
    expect(res.token).toBe('jwt-test')
    expect(res.role).toBe('USER')
    expect(global.fetch).toHaveBeenCalledWith(
      '/api/auth/login',
      expect.objectContaining({ method: 'POST' })
    )
  })

  it('register envía POST con username, email y password', async () => {
    mockFetch({ token: 'jwt-new', role: 'USER' })
    const res = await authApi.register('Ismael', 'ismael@test.com', 'pass')
    expect(res.token).toBe('jwt-new')
    expect(global.fetch).toHaveBeenCalledWith(
      '/api/auth/register',
      expect.objectContaining({ method: 'POST' })
    )
  })

  it('login lanza error si la respuesta no es ok', async () => {
    mockFetch({}, false, 401)
    await expect(authApi.login('x@x.com', 'wrong')).rejects.toThrow('401')
  })

  it('incluye Authorization header si hay token en localStorage', async () => {
    localStorage.setItem('token', 'mi-jwt')
    mockFetch({ token: 'jwt', role: 'USER' })
    await authApi.login('x@x.com', 'pass')
    const call = (global.fetch as ReturnType<typeof vi.fn>).mock.calls[0]
    const headers = call[1].headers
    expect(headers['Authorization']).toBe('Bearer mi-jwt')
  })
})

describe('productsApi', () => {
  it('getAll retorna lista de productos', async () => {
    const products = [{ id: 1, name: 'Producto A', price: 1000 }]
    mockFetch(products)
    const res = await productsApi.getAll()
    expect(res).toHaveLength(1)
    expect(res[0].name).toBe('Producto A')
  })

  it('getById retorna un producto por id', async () => {
    mockFetch({ id: 5, name: 'Producto B', price: 5000 })
    const res = await productsApi.getById(5)
    expect(res.id).toBe(5)
    expect(global.fetch).toHaveBeenCalledWith(
      '/api/products/5',
      expect.any(Object)
    )
  })

  it('getAll lanza error si la respuesta no es ok', async () => {
    mockFetch({}, false, 500)
    await expect(productsApi.getAll()).rejects.toThrow('500')
  })
})

describe('cartApi', () => {
  it('getCart retorna ítems del carrito por userId', async () => {
    const items = [{ id: 1, userId: 2, productId: 3, quantity: 1, unitPrice: 999 }]
    mockFetch(items)
    const res = await cartApi.getCart(2)
    expect(res).toHaveLength(1)
    expect(global.fetch).toHaveBeenCalledWith('/api/cart/2', expect.any(Object))
  })

  it('addItem envía POST con datos del ítem', async () => {
    const item = { userId: 1, productId: 2, quantity: 1, unitPrice: 500 }
    mockFetch({ id: 10, ...item })
    const res = await cartApi.addItem(item)
    expect(res.id).toBe(10)
    expect(global.fetch).toHaveBeenCalledWith(
      '/api/cart',
      expect.objectContaining({ method: 'POST' })
    )
  })

  it('removeItem envía DELETE por id', async () => {
    global.fetch = vi.fn().mockResolvedValue({ ok: true, status: 204, json: () => Promise.resolve(null) } as unknown as Response)
    await cartApi.removeItem(7)
    expect(global.fetch).toHaveBeenCalledWith(
      '/api/cart/7',
      expect.objectContaining({ method: 'DELETE' })
    )
  })

  it('clearCart envía DELETE para vaciar carrito del usuario', async () => {
    global.fetch = vi.fn().mockResolvedValue({ ok: true, status: 204, json: () => Promise.resolve(null) } as unknown as Response)
    await cartApi.clearCart(3)
    expect(global.fetch).toHaveBeenCalledWith(
      '/api/cart/clear/3',
      expect.objectContaining({ method: 'DELETE' })
    )
  })
})

describe('ordersApi', () => {
  it('create envía POST con datos de la orden', async () => {
    const order = { userId: 1, productId: 2, quantity: 1, status: 'PENDIENTE', totalPrice: 1000 }
    mockFetch({ id: 1, ...order })
    const res = await ordersApi.create(order)
    expect(res.id).toBe(1)
    expect(global.fetch).toHaveBeenCalledWith(
      '/api/orders',
      expect.objectContaining({ method: 'POST' })
    )
  })
})

describe('searchApi', () => {
  it('search retorna resultados por keyword', async () => {
    const results = [{ id: 1, productId: 1, name: 'Laptop', category: 'Electronics', price: 500000 }]
    mockFetch(results)
    const res = await searchApi.search('laptop')
    expect(res[0].name).toBe('Laptop')
    expect(global.fetch).toHaveBeenCalledWith(
      expect.stringContaining('keyword=laptop'),
      expect.any(Object)
    )
  })
})

describe('wishlistApi', () => {
  it('get retorna lista de deseos por userId', async () => {
    const items = [{ id: 1, userId: 1, productId: 5 }]
    mockFetch(items)
    const res = await wishlistApi.get(1)
    expect(res).toHaveLength(1)
    expect(global.fetch).toHaveBeenCalledWith('/api/wishlist/1', expect.any(Object))
  })

  it('add envía POST para agregar ítem', async () => {
    const item = { userId: 1, productId: 3 }
    mockFetch({ id: 2, ...item })
    const res = await wishlistApi.add(item)
    expect(res.id).toBe(2)
  })

  it('remove envía DELETE por id', async () => {
    global.fetch = vi.fn().mockResolvedValue({ ok: true, status: 204, json: () => Promise.resolve(null) } as unknown as Response)
    await wishlistApi.remove(4)
    expect(global.fetch).toHaveBeenCalledWith(
      '/api/wishlist/4',
      expect.objectContaining({ method: 'DELETE' })
    )
  })
})
