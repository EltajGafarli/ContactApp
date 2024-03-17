import './App.css';
import Header from "./components/Header";
import React, {Fragment, useEffect, useRef, useState} from "react";
import {getContacts, saveContact, updateContact, updatePhoto} from "./api/ContactService";
import {Navigate, Route, Routes} from "react-router-dom";
import ContactList from "./components/ContactList";
import { ToastContainer } from 'react-toastify';


function App() {
    const [data, setData] = useState({})
    const [currentPage, setCurrentPage] = useState(0);
    const modalRef = useRef();
    const [file, setFile] = useState(undefined);
    const [errorMessages, setErrorMessages] = useState([]);

    const [values, setValues] = useState(
        {
            name: '',
            email: '',
            phone: '',
            address: '',
            title: '',
            status: ''
        }
    );

    const fileRef = useRef();

    const onChange = (event) => {
        setValues({...values, [event.target.name]: event.target.value});
    }

    const getAllContacts = async (page = 0, size = 50) => {
        try {
            setCurrentPage(page);
            const {data} = await getContacts(page, size);
            setData(data);
        } catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        getAllContacts();
    }, []);

    const toggleModal = (show) => show ? modalRef.current.showModal() : modalRef.current.close();

    const handleNewContact = async (event) => {
        event.preventDefault();
        try{
            const { data } = await saveContact(values);
            const formData = new FormData();
            formData.append('id', data.id)
            formData.append('file', file, file.name);
            const {data: photoUrl} = await updatePhoto(formData);
            toggleModal(false);
            setFile(undefined);
            fileRef.current.value = null;
            setValues({
                name: '',
                email: '',
                phone: '',
                address: '',
                title: '',
                status: ''
            })
        } catch (error) {
            if (error.response && error.response.data) {
                console.log(error.response.data);
                setErrorMessages(error.response.data);
            } else {
                setErrorMessages(["An unexpected error occurred"]);
                console.log(error);
            }
        }
    }

    return (
    <Fragment>
        <Header toggleModal={toggleModal} nbOfContacts={data.totalElements} />
        <main className='main'>
            <div className='container'>
                <Routes>
                    <Route path='/' element={<Navigate to={'/contacts'} />} />
                    <Route path="/contacts" element={<ContactList data={data} currentPage={currentPage} getAllContacts={getAllContacts} />} />
                    {/*<Route path="/contacts/:id" element={<ContactDetails updateContact={updateContact} updateImage={updateImage} />} />*/}
                </Routes>
            </div>
        </main>

        {/* Modal */}
        <dialog ref={modalRef} className="modal" id="modal">
        <div className="modal__header">
            <h3>New Contact</h3>
            <i onClick={() => toggleModal(false)} className="bi bi-x-lg"></i>
        </div>
        <div className="divider"></div>
        <div className="modal__body">
            {(errorMessages.length > 0) && (
                <div className="error-messages">
                    {typeof errorMessages !== 'string' ? errorMessages.map((message, index) => (
                        <div key={index}>{message.defaultMessage}</div>
                    )) : <div>{errorMessages}</div>}
                </div>
            )}
            <form onSubmit={handleNewContact}>
                <div className="user-details">
                    <div className="input-box">
                        <span className="details">Name</span>
                        <input type="text" value={values.name} onChange={onChange} name='name' required />
                    </div>
                    <div className="input-box">
                        <span className="details">Email</span>
                        <input type="text" value={values.email} onChange={onChange} name='email' required />
                    </div>
                    <div className="input-box">
                        <span className="details">Title</span>
                        <input type="text" value={values.title} onChange={onChange} name='title' required />
                    </div>
                    <div className="input-box">
                        <span className="details">Phone Number</span>
                        <input type="text" value={values.phone} onChange={onChange} name='phone' required />
                    </div>
                    <div className="input-box">
                        <span className="details">Address</span>
                        <input type="text" value={values.address} onChange={onChange} name='address' required />
                    </div>
                    <div className="input-box">
                        <span className="details">Account Status</span>
                        <input type="text" value={values.status} onChange={onChange} name='status' required />
                    </div>
                    <div className="file-input">
                        <span className="details">Profile Photo</span>
                        <input type="file" onChange={(event) => setFile(event.target.files[0])} ref={fileRef} name='photo' required />
                    </div>
                </div>
                <div className="form_footer">
                    <button onClick={() => toggleModal(false)} type='button' className="btn btn-danger">Cancel</button>
                    <button type='submit' className="btn">Save</button>
                </div>
            </form>
        </div>
    </dialog>
        <ToastContainer />
    </Fragment>
  );
}

export default App;
