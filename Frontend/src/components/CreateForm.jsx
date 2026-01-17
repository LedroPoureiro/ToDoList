import { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

const CreateForm = ({ onCreate, onClickAI, llmSuggestion, onClearSuggestion }) => {
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [AItitle, setAITitle] = useState('')
  const [AIdescription, setAIDescription] = useState('')
  const [date, setDate] = useState(new Date());
  const navigate = useNavigate();

  const formatDate = (date) => {
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, "0");
    const dd = String(date.getDate()).padStart(2, "0");
    return `${yyyy}-${mm}-${dd}`;
  };

  const handleSave = (event) => {
    event.preventDefault()
    console.log(date)
    onCreate({
      title: title,
      description: description,
      dueDate: formatDate(date),
      completed: false
    })
  }

  const onClickButton = () => {
    setAITitle('')
    setAIDescription('')
    onClickAI()
  }

  useEffect(() => {
    if (llmSuggestion) {
      setAITitle(llmSuggestion || '')
      setAIDescription(llmSuggestion || '')
    }
  }, [llmSuggestion])

  useEffect(() => {
    return () => {
      // Cleanup when component unmounts (navigating away)
      if (onClearSuggestion) {
        onClearSuggestion()
      }
    }
  }, [onClearSuggestion])

  return (
    <div className="create-form-wrapper">
      <div className="create-form-container">
        <div className="form-header">
          <button type="button" className="back-button" onClick={() => navigate('/tasks')}>← Back</button>
          <h3>Create new task</h3>
        </div>
        <form onSubmit={handleSave} className="create-form">
          
          <div className="form-group">
            <label>Title</label>
            <input
              type="text"
              value={title}
              onChange={({ target }) => setTitle(target.value)}
              placeholder="Enter task title"
              required
            />
          </div>

          <div className="form-group">
            <label>Description</label>
            <input
              type="text"
              value={description}
              onChange={({ target }) => setDescription(target.value)}
              placeholder="Enter task description"
            />
          </div>

          <div className="form-group">
            <label>Due date</label>
            <DatePicker selected={date} onChange={(date) => setDate(date)} />
          </div>

          <button type="submit" className="save-button">Save</button>
        </form>
      </div>
      <div className="create-form-container">
        <button type="submit" className="suggest-button" onClick={() => onClickButton()}>Ask AI for a new task</button>
          <div className="form-group">
            <label>Title</label>
            <input
              type="text"
              value={AItitle}
              onChange={({ target }) => setAITitle(target.value)}
              readOnly
            />
          </div>

          <div className="form-group">
            <label>Description</label>
            <input
              type="text"
              value={AIdescription}
              onChange={({ target }) => setAIDescription(target.value)}
              readOnly
            />
          </div>


      </div>
    </div>
  )
}

export default CreateForm