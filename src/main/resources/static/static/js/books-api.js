async function deleteBook(bookId) {
    try {
        const response = await fetch(`/api/books/${bookId}`, {
            method: "DELETE",
            headers: {
                "Accept": "application/json"
            }
        });

        if (response.status === 204) {
            document.getElementById(`book-${bookId}`).remove();
        } else if (response.status === 404) {
            alert("Book not found.");
        } else {
            alert("Unexpected error.");
        }

    } catch (error) {
        console.error(error);
    }
}

async function loadBookDetails(bookId) {
    const response = await fetch(`/api/books/${bookId}`, {
        headers: {
            "Accept": "application/json"
        }
    });

    if (response.status === 200) {
        const book = await response.json();
        console.log(book);
    }
}