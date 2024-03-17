import React, {Fragment} from "react"

export default function Header({toggleModal, nbOfContacts}) {
    return (
        <Fragment>
            <header className='header'>
                <div className='container'>
                    <h3>Contact List ({nbOfContacts})</h3>
                    <button onClick={() => toggleModal(true)} className='btn'>
                        <i className='bi bi-plus-square'></i> Add New Contact
                    </button>
                </div>
            </header>
        </Fragment>
    )
}