import { Link } from 'react-router-dom'

const LoginForm = ({
  handleSubmit,
  handleUsernameChange,
  handlePasswordChange,
  username,
  password
}) => {
  return (
    <div className='center-screen'>
      <div className='login-form'>
        <h2 style={{ textAlign: 'center', marginBottom: '1.5rem', fontSize: '1.8rem', fontWeight: 'bold' }}>Login</h2>

        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 'bold' }}>
              username
              <input
                type="text"
                value={username}
                onChange={handleUsernameChange}
                style={{
                  width: '100%',
                  padding: '0.5rem',
                  border: '1px solid #ccc',
                  borderRadius: '4px',
                  boxSizing: 'border-box'
                }}
              />
            </label>
          </div>
          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 'bold' }}>
              password
              <input
                type="password"
                value={password}
                onChange={handlePasswordChange}
                style={{
                  width: '100%',
                  padding: '0.5rem',
                  border: '1px solid #ccc',
                  borderRadius: '4px',
                  boxSizing: 'border-box'
                }}
              />
            </label>
          </div>
          <p style={{ textAlign: 'center', marginBottom: '1rem' }}>
            Not registered yet?{' '}
            <Link to="/register" className="text-blue-600 underline">
              Create an account
            </Link>
          </p>
          <button type="submit" style={{
            width: '100%',
            padding: '0.75rem',
            backgroundColor: '#2563eb',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            fontWeight: 'bold',
            cursor: 'pointer'
          }}>Login</button>
        </form>
      </div>
    </div>
  )
}

export default LoginForm