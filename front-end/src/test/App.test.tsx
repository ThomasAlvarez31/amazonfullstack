import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { render, screen, fireEvent, waitFor, cleanup } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import { App } from '../App'

vi.mock('../api', () => ({
  authApi: {
    login: vi.fn(),
    register: vi.fn(),
  },
  productsApi: {
    getAll: vi.fn(),
    getById: vi.fn(),
  },
  cartApi: {
    getCart: vi.fn(),
    addItem: vi.fn(),
    removeItem: vi.fn(),
    clearCart: vi.fn(),
  },
  ordersApi: {
    create: vi.fn(),
  },
  searchApi: {
    search: vi.fn(),
  },
  wishlistApi: {
    get: vi.fn(),
    add: vi.fn(),
    remove: vi.fn(),
  },
}))

import { authApi, productsApi, cartApi, wishlistApi, searchApi, ordersApi } from '../api'

const mockProducts = [
  { id: 1, name: 'Laptop Pro', price: 799000 },
  { id: 2, name: 'Mouse Gamer', price: 25000 },
]

function renderApp(initialPath = '/index.html') {
  return render(
    <MemoryRouter initialEntries={[initialPath]}>
      <App />
    </MemoryRouter>
  )
}

beforeEach(() => {
  localStorage.clear()
  vi.clearAllMocks()
  vi.mocked(cartApi.getCart).mockResolvedValue([])
  vi.mocked(productsApi.getAll).mockResolvedValue(mockProducts)
  vi.mocked(wishlistApi.get).mockResolvedValue([])
  vi.mocked(searchApi.search).mockResolvedValue([])
})

afterEach(() => {
  cleanup()
})

describe('Home', () => {
  it('renderiza sección de productos destacados', async () => {
    renderApp('/index.html')
    await waitFor(() => {
      expect(screen.getByText('Productos destacados')).toBeInTheDocument()
    })
  })

  it('muestra productos cargados desde API', async () => {
    renderApp('/index.html')
    await waitFor(() => {
      expect(screen.getByText('Laptop Pro')).toBeInTheDocument()
      expect(screen.getByText('Mouse Gamer')).toBeInTheDocument()
    })
  })

  it('muestra links "Ver detalle" para cada producto', async () => {
    renderApp('/index.html')
    await waitFor(() => {
      const links = screen.getAllByText('Ver detalle')
      expect(links.length).toBe(2)
    })
  })

  it('maneja error de API y no rompe la UI', async () => {
    vi.mocked(productsApi.getAll).mockRejectedValue(new Error('Network error'))
    renderApp('/index.html')
    await waitFor(() => {
      expect(screen.getByText('Productos destacados')).toBeInTheDocument()
    })
  })
})

describe('Login', () => {
  it('renderiza formulario de login por defecto', async () => {
    renderApp('/login.html')
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Iniciar sesion' })).toBeInTheDocument()
    })
    expect(screen.getByPlaceholderText('Email')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument()
  })

  it('cambia a modo registro al hacer click en "Crear cuenta"', async () => {
    renderApp('/login.html')
    await waitFor(() => screen.getByText('Crear cuenta'))
    fireEvent.click(screen.getByText('Crear cuenta'))
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Registro' })).toBeInTheDocument()
      expect(screen.getByPlaceholderText('Usuario')).toBeInTheDocument()
    })
  })

  it('cambia de vuelta a login desde registro', async () => {
    renderApp('/login.html')
    await waitFor(() => fireEvent.click(screen.getByText('Crear cuenta')))
    await waitFor(() => screen.getByText('Ya tengo cuenta'))
    fireEvent.click(screen.getByText('Ya tengo cuenta'))
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Iniciar sesion' })).toBeInTheDocument()
    })
  })

  it('muestra error al fallar el login', async () => {
    vi.mocked(authApi.login).mockRejectedValue(new Error('401'))
    renderApp('/login.html')
    await waitFor(() => screen.getByPlaceholderText('Email'))
    fireEvent.change(screen.getByPlaceholderText('Email'), { target: { value: 'x@x.com' } })
    fireEvent.change(screen.getByPlaceholderText('Password'), { target: { value: 'wrong' } })
    fireEvent.click(screen.getByText('Continuar'))
    await waitFor(() => {
      expect(screen.getByText('Credenciales invalidas o usuario existente.')).toBeInTheDocument()
    })
  })

  it('guarda token en localStorage tras login exitoso', async () => {
    vi.mocked(authApi.login).mockResolvedValue({ token: 'test-token', role: 'USER' })
    renderApp('/login.html')
    await waitFor(() => screen.getByPlaceholderText('Email'))
    fireEvent.change(screen.getByPlaceholderText('Email'), { target: { value: 'user@test.com' } })
    fireEvent.change(screen.getByPlaceholderText('Password'), { target: { value: 'pass123' } })
    fireEvent.click(screen.getByText('Continuar'))
    await waitFor(() => {
      expect(localStorage.getItem('token')).toBe('test-token')
    })
  })

  it('registro exitoso guarda token y username', async () => {
    vi.mocked(authApi.register).mockResolvedValue({ token: 'reg-token', role: 'USER' })
    renderApp('/login.html')
    await waitFor(() => fireEvent.click(screen.getByText('Crear cuenta')))
    await waitFor(() => screen.getByPlaceholderText('Usuario'))
    fireEvent.change(screen.getByPlaceholderText('Email'), { target: { value: 'new@test.com' } })
    fireEvent.change(screen.getByPlaceholderText('Password'), { target: { value: 'pass' } })
    fireEvent.change(screen.getByPlaceholderText('Usuario'), { target: { value: 'Ismael' } })
    fireEvent.click(screen.getByText('Continuar'))
    await waitFor(() => {
      expect(localStorage.getItem('token')).toBe('reg-token')
      expect(localStorage.getItem('username')).toBe('Ismael')
    })
  })
})

describe('Navbar', () => {
  it('muestra "Identificate" cuando no hay sesión', async () => {
    renderApp('/index.html')
    await waitFor(() => {
      expect(screen.getByText(/Identificate/)).toBeInTheDocument()
    })
  })

  it('muestra nombre de usuario cuando hay sesión', async () => {
    localStorage.setItem('token', 'tok')
    localStorage.setItem('username', 'Ismael')
    renderApp('/index.html')
    await waitFor(() => {
      expect(screen.getByText(/Ismael/)).toBeInTheDocument()
    })
  })

  it('muestra botón Admin cuando el rol es ADMIN', async () => {
    localStorage.setItem('token', 'tok')
    localStorage.setItem('role', 'ADMIN')
    renderApp('/index.html')
    await waitFor(() => {
      const adminBtns = screen.getAllByText('Admin')
      expect(adminBtns.length).toBeGreaterThan(0)
    })
  })

  it('muestra contador de carrito con cantidad', async () => {
    vi.mocked(cartApi.getCart).mockResolvedValue([
      { id: 1, userId: 1, productId: 1, quantity: 3, unitPrice: 100 },
    ])
    renderApp('/index.html')
    await waitFor(() => {
      expect(screen.getByText('3')).toBeInTheDocument()
    })
  })
})

describe('Cart', () => {
  it('muestra heading Carrito', async () => {
    renderApp('/cart.html')
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Carrito' })).toBeInTheDocument()
    })
  })

  it('muestra ítems del carrito con total', async () => {
    vi.mocked(cartApi.getCart).mockResolvedValue([
      { id: 1, userId: 1, productId: 5, quantity: 2, unitPrice: 10000 },
    ])
    renderApp('/cart.html')
    await waitFor(() => {
      expect(screen.getByText('Producto #5')).toBeInTheDocument()
    })
    const totals = screen.getAllByText(/20\.000/)
    expect(totals.length).toBeGreaterThan(0)
  })

  it('botón "Ir a pagar" está presente', async () => {
    renderApp('/cart.html')
    await waitFor(() => {
      expect(screen.getByText('Ir a pagar')).toBeInTheDocument()
    })
  })
})

describe('Confirmation', () => {
  it('muestra heading de pedido confirmado', async () => {
    renderApp('/confirmation.html?total=50000')
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Pedido confirmado' })).toBeInTheDocument()
    })
  })

  it('muestra el total formateado', async () => {
    renderApp('/confirmation.html?total=50000')
    await waitFor(() => {
      expect(screen.getByText(/50\.000/)).toBeInTheDocument()
    })
  })

  it('tiene enlace para volver a la tienda', async () => {
    renderApp('/confirmation.html?total=0')
    await waitFor(() => {
      expect(screen.getByText('Volver a la tienda')).toBeInTheDocument()
    })
  })
})

describe('Admin', () => {
  it('redirige a login si el rol no es ADMIN', async () => {
    localStorage.setItem('token', 'tok')
    localStorage.setItem('role', 'USER')
    renderApp('/admin.html')
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Iniciar sesion' })).toBeInTheDocument()
    })
  })

  it('muestra panel de admin si el rol es ADMIN', async () => {
    localStorage.setItem('token', 'tok')
    localStorage.setItem('role', 'ADMIN')
    renderApp('/admin.html')
    await waitFor(() => {
      expect(screen.getByText(/Panel migrado a React/)).toBeInTheDocument()
    })
  })
})

describe('Footer', () => {
  it('renderiza el footer con año 2026', async () => {
    renderApp('/index.html')
    await waitFor(() => {
      expect(screen.getByText(/2026 - Recreacion academica/)).toBeInTheDocument()
    })
  })
})

describe('Wishlist', () => {
  it('muestra heading Lista de deseos', async () => {
    renderApp('/wishlist.html')
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Lista de deseos' })).toBeInTheDocument()
    })
  })

  it('muestra ítems de la lista de deseos', async () => {
    vi.mocked(wishlistApi.get).mockResolvedValue([{ id: 1, userId: 1, productId: 3 }])
    vi.mocked(productsApi.getAll).mockResolvedValue([{ id: 3, name: 'Teclado', price: 30000 }])
    renderApp('/wishlist.html')
    await waitFor(() => {
      expect(screen.getByText('Producto #3')).toBeInTheDocument()
    })
  })

  it('maneja error en wishlist y mantiene UI', async () => {
    vi.mocked(wishlistApi.get).mockRejectedValue(new Error('error'))
    renderApp('/wishlist.html')
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Lista de deseos' })).toBeInTheDocument()
    })
  })
})

describe('Navbar interactions', () => {
  it('búsqueda navega a products con keyword', async () => {
    vi.mocked(searchApi.search).mockResolvedValue([
      { id: 1, productId: 1, name: 'Laptop Pro', category: 'Tech', price: 799000 },
    ])
    renderApp('/index.html')
    await waitFor(() => screen.getByPlaceholderText('Buscar en Amazon.cl'))
    fireEvent.change(screen.getByPlaceholderText('Buscar en Amazon.cl'), { target: { value: 'laptop' } })
    const searchBtn = screen.getByText('⚲')
    fireEvent.click(searchBtn)
    await waitFor(() => {
      expect(screen.getByText(/Resultados para "laptop"/)).toBeInTheDocument()
    })
  })

  it('cerrar sesión limpia localStorage', async () => {
    localStorage.setItem('token', 'tok')
    localStorage.setItem('username', 'Ismael')
    renderApp('/index.html')
    await waitFor(() => screen.getByText(/Cerrar sesion/))
    fireEvent.click(screen.getByText(/Cerrar sesion/))
    await waitFor(() => {
      expect(localStorage.getItem('token')).toBeNull()
    })
  })

  it('click en logo navega a home', async () => {
    renderApp('/login.html')
    await waitFor(() => screen.getByRole('heading', { name: 'Iniciar sesion' }))
    fireEvent.click(screen.getByRole('img', { name: 'Amazon.cl' }).closest('button')!)
    await waitFor(() => {
      expect(screen.getByText('Productos destacados')).toBeInTheDocument()
    })
  })

  it('click en "Cuenta y listas" sin sesión va a login', async () => {
    renderApp('/index.html')
    await waitFor(() => screen.getByText(/Cuenta y listas/))
    fireEvent.click(screen.getByText(/Cuenta y listas/).closest('button')!)
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Iniciar sesion' })).toBeInTheDocument()
    })
  })

  it('click en Admin button del navbar va a admin', async () => {
    localStorage.setItem('token', 'tok')
    localStorage.setItem('role', 'ADMIN')
    renderApp('/index.html')
    await waitFor(() => screen.getAllByText('Admin'))
    const adminNavBtn = screen.getByRole('button', { name: 'Admin' })
    fireEvent.click(adminNavBtn)
    await waitFor(() => {
      expect(screen.getByText(/Panel migrado a React/)).toBeInTheDocument()
    })
  })

  it('click en wishlist navega a la lista', async () => {
    renderApp('/index.html')
    await waitFor(() => screen.getByText('Lista de deseos'))
    fireEvent.click(screen.getByText('Lista de deseos'))
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Lista de deseos' })).toBeInTheDocument()
    })
  })
})

describe('Products', () => {
  it('sin keyword muestra todos los productos', async () => {
    renderApp('/products.html')
    await waitFor(() => {
      expect(screen.getByText('Productos')).toBeInTheDocument()
      expect(screen.getByText('Laptop Pro')).toBeInTheDocument()
    })
  })

  it('con keyword busca via searchApi', async () => {
    vi.mocked(searchApi.search).mockResolvedValue([
      { id: 1, productId: 1, name: 'Laptop Pro', category: 'Tech', price: 799000 },
    ])
    renderApp('/products.html?q=laptop')
    await waitFor(() => {
      expect(screen.getByText(/Resultados para "laptop"/)).toBeInTheDocument()
      expect(screen.getByText('Laptop Pro')).toBeInTheDocument()
    })
  })

  it('con keyword cae a filtro local si searchApi falla', async () => {
    vi.mocked(searchApi.search).mockRejectedValue(new Error('404'))
    vi.mocked(productsApi.getAll).mockResolvedValue([
      { id: 1, name: 'Laptop Pro', price: 799000 },
      { id: 2, name: 'Mouse', price: 5000 },
    ])
    renderApp('/products.html?q=laptop')
    await waitFor(() => {
      expect(screen.getByText('Laptop Pro')).toBeInTheDocument()
    })
  })
})

describe('Checkout', () => {
  it('muestra el total del carrito', async () => {
    vi.mocked(cartApi.getCart).mockResolvedValue([
      { id: 1, userId: 1, productId: 1, quantity: 1, unitPrice: 50000 },
    ])
    renderApp('/checkout.html')
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Checkout' })).toBeInTheDocument()
      expect(screen.getByText(/50\.000/)).toBeInTheDocument()
    })
  })

  it('confirmar pago crea orden y vacía carrito', async () => {
    vi.mocked(cartApi.getCart).mockResolvedValue([
      { id: 1, userId: 1, productId: 2, quantity: 1, unitPrice: 20000 },
    ])
    vi.mocked(ordersApi.create).mockResolvedValue({ id: 1, userId: 1, productId: 2, quantity: 1, status: 'PENDIENTE', totalPrice: 20000 })
    vi.mocked(cartApi.clearCart).mockResolvedValue(undefined as unknown as void)
    renderApp('/checkout.html')
    await waitFor(() => screen.getByText('Confirmar y pagar'))
    fireEvent.click(screen.getByText('Confirmar y pagar'))
    await waitFor(() => {
      expect(ordersApi.create).toHaveBeenCalled()
      expect(cartApi.clearCart).toHaveBeenCalled()
    })
  })
})

describe('Detail', () => {
  it('muestra mensaje si el producto no se encuentra', async () => {
    vi.mocked(productsApi.getById).mockRejectedValue(new Error('404'))
    renderApp('/detail.html?id=999')
    await waitFor(() => {
      expect(screen.getByText('Producto no encontrado.')).toBeInTheDocument()
    })
  })

  it('muestra detalle del producto', async () => {
    vi.mocked(productsApi.getById).mockResolvedValue({ id: 1, name: 'Laptop Pro', price: 799000 })
    renderApp('/detail.html?id=1')
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Laptop Pro' })).toBeInTheDocument()
    })
  })
})
