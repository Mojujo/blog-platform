import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { AuthProvider } from './context/AuthContext.tsx'
import { ScreenProvider } from './context/ScreenContext.tsx'
import { BookmarkProvider } from './context/BookmarkContext.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <AuthProvider>
      <ScreenProvider>
        <BookmarkProvider>
          <App />
        </BookmarkProvider>
      </ScreenProvider>
    </AuthProvider>
  </StrictMode>,
)
