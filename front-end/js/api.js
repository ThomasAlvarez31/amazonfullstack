const BASE = {
    auth: 'http://localhost:9000',
    users: 'http://localhost:8082',
    products: 'http://localhost:8085',
    cart: 'http://localhost:8087',
    orders: 'http://localhost:8084',
    reviews: 'http://localhost:8090',
    search: 'http://localhost:8091',
    wishlist: 'http://localhost:8092',
};
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
    login: (email, password) => post(`${BASE.auth}/api/auth/login`, { email, password }),
    register: (username, email, password) => post(`${BASE.auth}/api/auth/register`, { username, email, password }),
};
export const productsApi = {
    getAll: () => get(`${BASE.products}/api/products`),
    getById: (id) => get(`${BASE.products}/api/products/${id}`),
};
export const cartApi = {
    getCart: (userId) => get(`${BASE.cart}/api/cart/${userId}`),
    addItem: (item) => post(`${BASE.cart}/api/cart`, item),
    removeItem: (id) => del(`${BASE.cart}/api/cart/${id}`),
    clearCart: (userId) => del(`${BASE.cart}/api/cart/clear/${userId}`),
};
export const ordersApi = {
    create: (order) => post(`${BASE.orders}/api/orders`, order),
};
export const reviewsApi = {
    getByProduct: (productId) => get(`${BASE.reviews}/api/reviews/product/${productId}`),
    create: (review) => post(`${BASE.reviews}/api/reviews`, review),
};
export const searchApi = {
    search: (keyword) => get(`${BASE.search}/api/search?keyword=${encodeURIComponent(keyword)}`),
};
export const wishlistApi = {
    get: (userId) => get(`${BASE.wishlist}/api/wishlist/${userId}`),
    add: (item) => post(`${BASE.wishlist}/api/wishlist`, item),
    remove: (id) => del(`${BASE.wishlist}/api/wishlist/${id}`),
};
