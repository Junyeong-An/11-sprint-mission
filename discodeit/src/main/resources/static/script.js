// API endpoints
const API_BASE_URL = '/api';
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/user/findAll`,
    BINARY_CONTENT: `${API_BASE_URL}/binaryContent/find`
};
const DEFAULT_AVATAR = buildDefaultAvatarDataUrl();
const SAMPLE_USERS = [
    {
        username: 'jessie',
        email: 'jessie@codeit.com',
        online: true,
        avatarUrl: 'https://commons.wikimedia.org/wiki/Special:FilePath/Jessie%20at%20Shanghai%20Toy%20Story%20Land%20%28cropped%29.jpg'
    },
    {
        username: 'rex',
        email: 'rex@codeit.com',
        online: true,
        avatarUrl: 'https://commons.wikimedia.org/wiki/Special:FilePath/Hong%20Kong%20Disneyland%20%2824176189482%29%20%28cropped%29.jpg'
    },
    {
        username: 'buzz',
        email: 'buzz@codeit.com',
        online: true,
        avatarUrl: 'https://commons.wikimedia.org/wiki/Special:FilePath/Buzz%20Lightyear%20sculpture%20of%20Toy%20Story%20Hotel%20Shanghai%20%28cropped%29.jpg'
    },
    {
        username: 'woody',
        email: 'woody@codeit.com',
        online: true,
        avatarUrl: 'https://commons.wikimedia.org/wiki/Special:FilePath/Woody%20at%20Toy%20Story%20Land%2C%20Hong%20Kong.jpg'
    }
];
const USE_SAMPLE_USERS = true;

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
    if (USE_SAMPLE_USERS) {
        renderUserList(SAMPLE_USERS);
        return;
    }
    fetchAndRenderUsers();
});

// Fetch users from the API
async function fetchAndRenderUsers() {
    try {
        const response = await fetch(ENDPOINTS.USERS);
        if (!response.ok) throw new Error('Failed to fetch users');
        const payload = await response.json();
        const users = payload.data ?? payload;
        renderUserList(users);
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

// Fetch user profile image
async function fetchUserProfile(profileId) {
    try {
        const response = await fetch(`${ENDPOINTS.BINARY_CONTENT}?binaryContentId=${profileId}`);
        if (!response.ok) throw new Error('Failed to fetch profile');
        const payload = await response.json();
        const profile = payload.data ?? payload;

        if (!profile.bytes) {
            return DEFAULT_AVATAR;
        }

        const contentType = profile.contentType && profile.contentType.trim()
            ? profile.contentType
            : 'application/octet-stream';
        return `data:${contentType};base64,${profile.bytes}`;
    } catch (error) {
        console.error('Error fetching profile:', error);
        return DEFAULT_AVATAR;
    }
}

// Render user list
async function renderUserList(users) {
    const userListElement = document.getElementById('userList');
    userListElement.innerHTML = ''; // Clear existing content

    for (const user of users) {
        const userElement = document.createElement('div');
        userElement.className = 'user-item';

        // Get profile image URL
        const profileUrl = user.avatarUrl
            ? user.avatarUrl
            : user.profileId
                ? await fetchUserProfile(user.profileId)
                : DEFAULT_AVATAR;

        userElement.innerHTML = `
            <img src="${profileUrl}" alt="${user.username}" class="user-avatar">
            <div class="user-info">
                <div class="user-name">${user.username}</div>
                <div class="user-email">${user.email}</div>
            </div>
            <div class="status-badge ${user.online ? 'online' : 'offline'}">
                ${user.online ? '온라인' : '오프라인'}
            </div>
        `;

        userListElement.appendChild(userElement);
    }
}

function buildDefaultAvatarDataUrl() {
    const svg = `
<svg xmlns="http://www.w3.org/2000/svg" width="120" height="120" viewBox="0 0 120 120">
  <rect width="120" height="120" fill="#e5e7eb"/>
  <circle cx="60" cy="44" r="22" fill="#9ca3af"/>
  <rect x="28" y="76" width="64" height="30" rx="15" fill="#9ca3af"/>
</svg>`;
    return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg.trim())}`;
}
