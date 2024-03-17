import {Fragment} from "react";
import {Link} from "react-router-dom";

export default function Contact({contact}) {
    return (
        <Fragment>
            <Link className="contact__item" to={`/contacts/${contact.id}`}>
                <div className="contact__header">
                    <div className="contact__image">
                        <img src={contact.photoUrl} alt={contact.name} />
                    </div>
                    <div className="contact__details">
                        <p className="contact__name"> {contact.name}</p>
                        <p className="contact__title"> {contact.title}</p>
                    </div>
                </div>
                <div className="contact__body">
                    <p><i className="bi bi-envelope"></i> {contact.email}</p>
                    <p><i className="bi bi-geo"></i> {contact.address}</p>
                    <p><i className="bi bi-telephone"></i> {contact.phone}</p>
                    <p>{contact.status === "Active" ? <i className="bi bi-check-circle"></i> :
                        <i className="bi bi-x-circle"></i>} {contact.status}
                    </p>
                </div>
            </Link>
        </Fragment>
    )
}