const BASE = 'http://localhost:8080';
function headers() {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {})
    };
}
async function get(url) {
    const res = await fetch(url, { headers: headers() });
    if (!res.ok)
        throw new Error(`${res.status}`);
    return res.json();
}
async function post(url, body) {
    const res = await fetch(url, { method: 'POST', headers: headers(), body: JSON.stringify(body) });
    if (!res.ok)
        throw new Error(`${res.status}`);
    return res.json();
}
async function del(url) {
    await fetch(url, { method: 'DELETE', headers: headers() });
}
export const authApi = {
    login: (email, password) => post(`${BASE}/api/auth/login`, { email, password }),
    register: (username, email, password) => post(`${BASE}/api/auth/register`, { username, email, password }),
};
export const productsApi = {
    getAll: () => get(`${BASE}/api/products`),
    getById: (id) => get(`${BASE}/api/products/${id}`),
};
export const cartApi = {
    getCart: (userId) => get(`${BASE}/api/cart/${userId}`),
    addItem: (item) => post(`${BASE}/api/cart`, item),
    removeItem: (id) => del(`${BASE}/api/cart/${id}`),
    clearCart: (userId) => del(`${BASE}/api/cart/clear/${userId}`),
};
export const ordersApi = {
    create: (order) => post(`${BASE}/api/orders`, order),
};
export const reviewsApi = {
    getByProduct: (productId) => get(`${BASE}/api/reviews/product/${productId}`),
    create: (review) => post(`${BASE}/api/reviews`, review),
};
export const searchApi = {
    search: (keyword) => get(`${BASE}/api/search?keyword=${encodeURIComponent(keyword)}`),
};
export const wishlistApi = {
    get: (userId) => get(`${BASE}/api/wishlist/${userId}`),
    add: (item) => post(`${BASE}/api/wishlist`, item),
    remove: (id) => del(`${BASE}/api/wishlist/${id}`),
};
