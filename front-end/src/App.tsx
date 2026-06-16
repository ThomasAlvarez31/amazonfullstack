import { FormEvent, useEffect, useState } from 'react'
import { Link, Navigate, Route, Routes, useLocation, useNavigate, useSearchParams } from 'react-router-dom'
import { authApi, cartApi, ordersApi, productsApi, searchApi, wishlistApi } from './api'
import type { CartItem, Product } from './types'

const userId = Number(localStorage.getItem('userId') ?? 1)

function fmt(n: number): string { return `$${n.toLocaleString('es-CL')}` }

function Navbar() {
  const navigate = useNavigate()
  const location = useLocation()
  const [q, setQ] = useState('')
  const [count, setCount] = useState(0)
  const name = localStorage.getItem('username') ?? 'Identificate'
  const logged = !!localStorage.getItem('token')
  const admin = localStorage.getItem('role') === 'ADMIN'

  useEffect(() => {
    void cartApi.getCart(userId).then(items => setCount(items.reduce((s, i) => s + i.quantity, 0))).catch(() => {})
  }, [location.pathname])

  return <>
    <nav className="navbar">
      <button className="navbar__logo" onClick={() => navigate('/index.html')} style={{ background: 'none', border: 'none', lineHeight: 0 }}>
        <img src="/assets/logo.svg" height={36} alt="Amazon.cl" />
      </button>
      <div className="navbar__location"><span>Entregar a</span><span>Chile</span></div>
      <div className="navbar__search">
        <select><option>Todo</option></select>
        <input value={q} onChange={e => setQ(e.target.value)} placeholder="Buscar en Amazon.cl" />
        <button onClick={() => q.trim() && navigate(`/products.html?q=${encodeURIComponent(q.trim())}`)}>&#9906;</button>
      </div>
      <button className="navbar__item" onClick={() => {
        if (logged) { localStorage.clear(); navigate('/index.html') } else navigate('/login.html')
      }} style={{ cursor: 'pointer', background: 'none', border: 'none', color: 'inherit', textAlign: 'left' }}>
        <span>Hola, {logged ? name : 'Identificate'}</span><span>{logged ? 'Cerrar sesion' : 'Cuenta y listas'}</span>
      </button>
      <button className="navbar__iconbtn" onClick={() => navigate('/wishlist.html')}>Lista de deseos</button>
      <button className="navbar__iconbtn" onClick={() => navigate('/cart.html')}>Carrito <span className="navbar__badge">{count}</span></button>
      {admin && <button className="navbar__admin" onClick={() => navigate('/admin.html')}>Admin</button>}
    </nav>
  </>
}

function Footer() { return <div className="footer-bottom"><p style={{ color: '#777', fontSize: 11 }}>2026 - Recreacion academica</p></div> }

function Home() {
  const [products, setProducts] = useState<Product[]>([])
  useEffect(() => { productsApi.getAll().then(setProducts).catch(() => setProducts([])) }, [])
  return <div className="container"><h2 className="section-title">Productos destacados</h2><div className="products-grid">{products.slice(0, 8).map(p =>
    <div key={p.id} className="product-card"><div className="product-card__img">{p.name[0]}</div><div className="product-card__name">{p.name}</div><div className="product-card__price"><sup>$</sup>{p.price.toLocaleString('es-CL')}</div><Link to={`/detail.html?id=${p.id}`}>Ver detalle</Link></div>
  )}</div></div>
}

function Products() {
  const [params] = useSearchParams()
  const q = params.get('q') ?? ''
  const [items, setItems] = useState<Product[]>([])
  useEffect(() => {
    if (!q) { productsApi.getAll().then(setItems).catch(() => setItems([])); return }
    searchApi.search(q).then(r => setItems(r.map(x => ({ id: x.productId, name: x.name, price: x.price })))).catch(async () => {
      const all = await productsApi.getAll(); setItems(all.filter(x => x.name.toLowerCase().includes(q.toLowerCase())))
    })
  }, [q])
  return <div className="container"><h2 className="section-title">{q ? `Resultados para "${q}"` : 'Productos'}</h2><div className="products-grid">{items.map(p =>
    <div key={p.id} className="product-card"><div className="product-card__img">{p.name[0]}</div><div className="product-card__name">{p.name}</div><div className="product-card__price"><sup>$</sup>{p.price.toLocaleString('es-CL')}</div><button className="btn-orange" onClick={() => cartApi.addItem({ userId, productId: p.id, quantity: 1, unitPrice: p.price })}>Agregar al carrito</button></div>
  )}</div></div>
}

function Detail() {
  const [params] = useSearchParams()
  const id = Number(params.get('id'))
  const [p, setP] = useState<Product | null>(null)
  useEffect(() => { if (id) productsApi.getById(id).then(setP).catch(() => setP(null)) }, [id])
  if (!p) return <div className="container"><p>Producto no encontrado.</p></div>
  return <div className="container"><h1>{p.name}</h1><p>{fmt(p.price)}</p><button className="btn-orange" onClick={() => cartApi.addItem({ userId, productId: p.id, quantity: 1, unitPrice: p.price })}>Agregar al carrito</button></div>
}

function Cart() {
  const [items, setItems] = useState<CartItem[]>([])
  const navigate = useNavigate()
  const load = () => { void cartApi.getCart(userId).then(setItems).catch(() => setItems([])) }
  useEffect(load, [])
  const total = items.reduce((s, i) => s + i.unitPrice * i.quantity, 0)
  return <div className="container"><h1>Carrito</h1>{items.map(i => <div key={i.id} className="cart-item"><div>Producto #{i.productId}</div><div>{fmt(i.unitPrice * i.quantity)}</div><button onClick={async () => { await cartApi.removeItem(i.id); load() }}>Eliminar</button></div>)}<h3>Total: {fmt(total)}</h3><button className="btn-orange" onClick={() => navigate('/checkout.html')}>Ir a pagar</button></div>
}

function Checkout() {
  const [items, setItems] = useState<CartItem[]>([])
  const navigate = useNavigate()
  useEffect(() => { cartApi.getCart(userId).then(setItems).catch(() => setItems([])) }, [])
  const total = items.reduce((s, i) => s + i.unitPrice * i.quantity, 0)
  return <div className="container"><h1>Checkout</h1><p>Total: {fmt(total)}</p><button className="btn-orange" onClick={async () => {
    for (const i of items) await ordersApi.create({ userId, productId: i.productId, quantity: i.quantity, status: 'PENDIENTE', totalPrice: i.unitPrice * i.quantity })
    await cartApi.clearCart(userId)
    navigate(`/confirmation.html?total=${total}`)
  }}>Confirmar y pagar</button></div>
}

function Wishlist() {
  const [rows, setRows] = useState<{ id: number, productId: number, price: number }[]>([])
  const load = async () => {
    const [wish, products] = await Promise.all([wishlistApi.get(userId), productsApi.getAll()])
    const map = new Map(products.map(p => [p.id, p.price]))
    setRows(wish.map(w => ({ id: Number(w.id), productId: w.productId, price: map.get(w.productId) ?? 0 })))
  }
  useEffect(() => { load().catch(() => setRows([])) }, [])
  return <div className="container"><h1>Lista de deseos</h1>{rows.map(r => <div key={r.id}><span>Producto #{r.productId}</span><button onClick={async () => { await cartApi.addItem({ userId, productId: r.productId, quantity: 1, unitPrice: r.price }); await wishlistApi.remove(r.id); load() }}>Mover al carrito</button></div>)}</div>
}

function Login() {
  const [mode, setMode] = useState<'login'|'register'>('login')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [username, setUsername] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()
  const submit = async (e: FormEvent) => {
    e.preventDefault(); setError('')
    try {
      const res = mode === 'login' ? await authApi.login(email, password) : await authApi.register(username, email, password)
      localStorage.setItem('token', res.token); localStorage.setItem('role', res.role)
      if (username) localStorage.setItem('username', username)
      navigate(res.role === 'ADMIN' ? '/admin.html' : '/index.html')
    } catch { setError('Credenciales invalidas o usuario existente.') }
  }
  return <div className="container"><h1>{mode === 'login' ? 'Iniciar sesion' : 'Registro'}</h1><form onSubmit={submit}><input placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} /><input placeholder="Password" type="password" value={password} onChange={e => setPassword(e.target.value)} />{mode === 'register' && <input placeholder="Usuario" value={username} onChange={e => setUsername(e.target.value)} />}<button type="submit" className="btn-orange">Continuar</button></form><button onClick={() => setMode(mode === 'login' ? 'register' : 'login')}>{mode === 'login' ? 'Crear cuenta' : 'Ya tengo cuenta'}</button>{error && <p>{error}</p>}</div>
}

function Admin() {
  if (localStorage.getItem('role') !== 'ADMIN') return <Navigate to="/login.html" replace />
  return <div className="container"><h1>Admin</h1><p>Panel migrado a React. Puedes ampliar este modulo segun tu flujo.</p></div>
}

function Confirmation() {
  const [params] = useSearchParams()
  return <div className="container"><h1>Pedido confirmado</h1><p>Total: {fmt(Number(params.get('total') ?? 0))}</p><Link to="/index.html">Volver a la tienda</Link></div>
}

export function App() {
  return <>
    <Navbar />
    <Routes>
      <Route path="/" element={<Navigate to="/index.html" replace />} />
      <Route path="/index.html" element={<Home />} />
      <Route path="/products.html" element={<Products />} />
      <Route path="/detail.html" element={<Detail />} />
      <Route path="/cart.html" element={<Cart />} />
      <Route path="/checkout.html" element={<Checkout />} />
      <Route path="/wishlist.html" element={<Wishlist />} />
      <Route path="/login.html" element={<Login />} />
      <Route path="/admin.html" element={<Admin />} />
      <Route path="/confirmation.html" element={<Confirmation />} />
      <Route path="*" element={<Navigate to="/index.html" replace />} />
    </Routes>
    <Footer />
  </>
}
