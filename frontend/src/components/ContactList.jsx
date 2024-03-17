import {Fragment} from "react";
import Contact from "./Contact";

export default function ContactList({data, currentPage, getAllContacts}) {
    return (
        <Fragment>
            <main className="main">
                {data?.content?.length === 0 && <div>No Contacts. Please add a new contact</div>}
                <ul className="contact__list">
                    {data?.content?.length > 0 && data.content.map(contact => {
                       return <Contact contact={contact} key={contact.id}></Contact>
                    })}
                </ul>

                {data?.content?.length > 0 && data?.totalPages > 1 &&
                    <div className='pagination'>
                        <a onClick={() => getAllContacts(currentPage - 1)} className={0 === currentPage ? 'disabled' : ''}>&laquo;</a>

                        { data && [...Array(data.totalPages).keys()].map((page, index) =>
                            <a onClick={() => getAllContacts(page)} className={currentPage === page ? 'active' : ''} key={page}>{page + 1}</a>)}


                        <a onClick={() => getAllContacts(currentPage + 1)} className={data.totalPages === currentPage + 1 ? 'disabled' : ''}>&raquo;</a>
                    </div>
                }
            </main>
        </Fragment>
    )
}