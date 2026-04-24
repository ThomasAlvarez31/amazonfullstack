import { renderNavbar, renderFooter } from './utils.js';
renderNavbar(document.getElementById('navbar'));
renderFooter(document.getElementById('footer'));
const BASE = 'http://localhost:8080';
function headers() {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {})
    };
}
// Tabs
document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
        btn.classList.add('active');
        document.getElementById(`tab-${btn.dataset['tab']}`)?.classList.add('active');
    });
});
// Products
async function loadProducts() {
    const tbody = document.getElementById('products-table');
    try {
        const res = await fetch(`${BASE}/api/products`, { headers: headers() });
        const products = await res.json();
        tbody.innerHTML = products.length
            ? products.map(p => `
          <tr>
            <td>${p.id}</td>
            <td>${p.name}</td>
            <td>$${p.price.toLocaleString('es-CL')}</td>
            <td>${p.category ?? '—'}</td>
          </tr>`).join('')
            : '<tr><td colspan="4" style="color:#555">Sin productos</td></tr>';
    }
    catch {
        tbody.innerHTML = '<tr><td colspan="4" style="color:#b12704">Error al cargar productos</td></tr>';
    }
}
document.getElementById('btn-load-products')?.addEventListener('click', loadProducts);
document.getElementById('btn-create-product')?.addEventListener('click', async () => {
    const name = document.getElementById('p-name').value.trim();
    const price = Number(document.getElementById('p-price').value);
    const desc = document.getElementById('p-desc').value.trim();
    const category = document.getElementById('p-category').value.trim();
    const result = document.getElementById('product-result');
    if (!name || !price) {
        result.textContent = 'Nombre y precio son obligatorios';
        result.className = 'admin-result error';
        return;
    }
    try {
        const res = await fetch(`${BASE}/api/products`, {
            method: 'POST',
            headers: headers(),
            body: JSON.stringify({ name, price, description: desc || undefined, category: category || undefined })
        });
        if (!res.ok)
            throw new Error(String(res.status));
        const p = await res.json();
        result.textContent = `Producto creado con ID ${p.id}`;
        result.className = 'admin-result';
        document.getElementById('p-name').value = '';
        document.getElementById('p-price').value = '';
        document.getElementById('p-desc').value = '';
        document.getElementById('p-category').value = '';
        loadProducts();
    }
    catch {
        result.textContent = 'Error al crear el producto';
        result.className = 'admin-result error';
    }
});
// Users
async function loadUsers() {
    const tbody = document.getElementById('users-table');
    try {
        const res = await fetch(`${BASE}/api/users`, { headers: headers() });
        const users = await res.json();
        tbody.innerHTML = users.length
            ? users.map(u => `
          <tr>
            <td>${u.id}</td>
            <td>${u.username ?? '—'}</td>
            <td>${u.email}</td>
          </tr>`).join('')
            : '<tr><td colspan="3" style="color:#555">Sin usuarios</td></tr>';
    }
    catch {
        tbody.innerHTML = '<tr><td colspan="3" style="color:#b12704">Error al cargar usuarios</td></tr>';
    }
}
document.getElementById('btn-load-users')?.addEventListener('click', loadUsers);
document.getElementById('btn-create-user')?.addEventListener('click', async () => {
    const username = document.getElementById('u-username').value.trim();
    const email = document.getElementById('u-email').value.trim();
    const password = document.getElementById('u-password').value;
    const result = document.getElementById('user-result');
    if (!username || !email || !password) {
        result.textContent = 'Todos los campos son obligatorios';
        result.className = 'admin-result error';
        return;
    }
    try {
        const res = await fetch(`${BASE}/api/users`, {
            method: 'POST',
            headers: headers(),
            body: JSON.stringify({ username, email, password })
        });
        if (!res.ok)
            throw new Error(String(res.status));
        const u = await res.json();
        result.textContent = `Usuario creado con ID ${u.id}`;
        result.className = 'admin-result';
        document.getElementById('u-username').value = '';
        document.getElementById('u-email').value = '';
        document.getElementById('u-password').value = '';
        loadUsers();
    }
    catch {
        result.textContent = 'Error al crear el usuario';
        result.className = 'admin-result error';
    }
});
loadProducts();
loadUsers();
